package validez.help;

import lombok.AllArgsConstructor;
import validez.lib.annotation.validators.ByteBound;
import validez.lib.annotation.validators.IntBound;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.LongBound;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NullValueStrategy;
import validez.lib.annotation.validators.ShortBound;
import validez.lib.annotation.validators.StringRange;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static validez.help.TestUtils.RANDOM;
import static validez.help.TestUtils.stringOfLen;

public class ObjectGenerator {

    public <T> T generateValid(Class<T> objectClass)
            throws InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException {

        Field[] fields = objectClass.getDeclaredFields();
        Constructor<T> constructor = objectClass.getDeclaredConstructor();
        T object = constructor.newInstance();
        for (Field field : fields) {
            List<Object> validValues = getValidValues(field, object);
            for (Object validValue : validValues) {
                setValue(field, validValue, object);
            }
        }
        return object;
    }

    public <T> List<T> generateInvalid(Class<T> objectClass)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Field[] fields = objectClass.getDeclaredFields();
        LinkedList<Field> fieldQueue = new LinkedList<>();
        for (Field field : fields) {
            fieldQueue.offer(field);
        }
        List<T> objects = new ArrayList<>();
        int index = 0;
        while (index < fields.length) {
            Field field = fieldQueue.poll();
            assert field != null;
            List<Object> invalidValues = getInvalidValues(field);
            for (Object invalidValue : invalidValues) {

                Map<Field, List<Object>> fieldToValues = new HashMap<>();
                int max = 0;
                for (Field validField : fieldQueue) {
                    List<Object> validValues = getValidValues(validField, null);
                    max = Math.max(validValues.size(), max);
                    fieldToValues.put(validField, validValues);
                }
                List<T> objs = new ArrayList<>();
                for (int i = 0; i < max; i++) {
                    Constructor<T> constructor = objectClass.getDeclaredConstructor();
                    T object = constructor.newInstance();
                    setValue(field, invalidValue, object);
                    objs.add(object);
                }
                for (Map.Entry<Field, List<Object>> entry : fieldToValues.entrySet()) {
                    Field f = entry.getKey();
                    List<Object> values = entry.getValue();
                    if (values.size() < max) {
                        int toAdd = max - values.size();
                        while (toAdd > 0) {
                            values.add(values.get(0));
                            toAdd--;
                        }
                    }
                    int i = 0;
                    for (Object value : values) {
                        T object = objs.get(i);
                        setValue(f, value, object);
                    }
                }
                objects.addAll(objs);
            }
            fieldQueue.offer(field);
            index++;
        }
        return objects;
    }

    private List<Object> getInvalidValues(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations.length == 0) {
            return Collections.emptyList();
        }
        List<Object> values = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof IntBound ||
                    annotation instanceof LongBound ||
                    annotation instanceof ShortBound ||
                    annotation instanceof ByteBound) {
                values.add(getInvalidForBound(field, annotation));
            } else if (annotation instanceof Length) {
                values.add(getInvalidForLength((Length) annotation));
            } else if (annotation instanceof IntRange) {
                values.add(getInvalidForIntRange((IntRange) annotation));
            } else if (annotation instanceof LongRange) {
                values.add(getInvalidForLongRange((LongRange) annotation));
            } else if (annotation instanceof StringRange) {
                //simple, expecting, no that uuid can be in annotation value range
                values.add(UUID.randomUUID().toString());
            } else if (annotation instanceof NotEmpty) {
                if (List.class.isAssignableFrom(field.getType())) {
                    values.add(new ArrayList<>());
                } else if (Set.class.isAssignableFrom(field.getType())) {
                    values.add(new HashSet<>());
                } else if (String.class.isAssignableFrom(field.getType())) {
                    values.add("");
                } else {
                    values.add(null);
                }
            }
        }
        return values;
    }

    private long getInvalidForLongRange(LongRange longRange) {
        long[] range = longRange.value();
        long randomLong = RANDOM.nextLong();
        int index = 0;
        while (index < range.length) {
            long val = range[index];
            if (val == randomLong) {
                index = 0;
                randomLong = RANDOM.nextLong();
            } else {
                index++;
            }
        }
        return randomLong;
    }

    private int getInvalidForIntRange(IntRange intRange) {
        int[] range = intRange.value();
        int randomInt = RANDOM.nextInt();
        int index = 0;
        while (index < range.length) {
            int val = range[index];
            if (val == randomInt) {
                index = 0;
                randomInt = RANDOM.nextInt();
            } else {
                index++;
            }
        }
        return randomInt;
    }

    private String getInvalidForLength(Length length) {
        int equals = length.equals();
        if (equals != Integer.MIN_VALUE) {
            return stringOfLen(equals - 1);
        }
        int max = length.max();
        int min = length.min();
        if (max != Integer.MAX_VALUE && min != Integer.MIN_VALUE) {
            int index = RANDOM.nextInt(0, 1);
            if (index == 0) {
                if (min - 1 < 0) {
                    return "";
                }
                return stringOfLen(min - 1);
            } else {
                return stringOfLen(max + 1);
            }
        } else if (min != Integer.MIN_VALUE) {
            if (min - 1 < 0) {
                return "";
            }
            return stringOfLen(min - 1);
        }
        return stringOfLen(max + 1);
    }

    private Object getInvalidForBound(Field field, Annotation boundAnnotation) {
        Bound bound = new BoundExtractor().extract(boundAnnotation);
        if (!field.getType().isPrimitive()) {
            NullValueStrategy nullValueStrategy = bound.nullValueStrategy;
            if (nullValueStrategy.equals(NullValueStrategy.NULL_NOT_ALLOWED)) {
                int rand = RANDOM.nextInt(100);
                if (rand % 100 == 0) {
                    return null;
                }
            }
        }
        long equals = bound.equals;
        if (equals != bound.minBound) {
            return randomOutOfBounds(equals - 1, equals + 1, bound.minBound, bound.maxBound,
                    bound.valueConverter);
        } else {
            long min = bound.min;
            long max = bound.max;
            if (min != bound.minBound && max != bound.maxBound) {
                return randomOutOfBounds(min, max, bound.minBound, bound.maxBound, bound.valueConverter);
            } else if (min != bound.minBound) {
                return randomNumber(bound.minBound, min - 1, bound.valueConverter);
            }
            return randomNumber(max + 1, bound.maxBound, bound.valueConverter);
        }
    }

    private <T> List<Object> getValidValues(Field field, T object) {
        Annotation[] annotations = field.getAnnotations();
        if (annotations.length == 0) {
            return Collections.emptyList();
        }
        List<Object> values = new ArrayList<>();
        boolean injectNotEmpty = true;
        for (Annotation annotation : annotations) {
            if (annotation instanceof IntBound ||
                    annotation instanceof LongBound ||
                    annotation instanceof ShortBound ||
                    annotation instanceof ByteBound) {
                values.add(getValidForBound(field, annotation));
                injectNotEmpty = false;
            } else if (annotation instanceof Length) {
                values.add(getValidForLength((Length) annotation));
            } else if (annotation instanceof IntRange) {
                IntRange intRange = (IntRange) annotation;
                values.add(getRandomValFromArray(intRange.value()));
                injectNotEmpty = false;
            } else if (annotation instanceof LongRange) {
                LongRange longRange = (LongRange) annotation;
                values.add(getRandomValFromArray(longRange.value()));
                injectNotEmpty = false;
            } else if (annotation instanceof StringRange) {
                StringRange stringRange = (StringRange) annotation;
                values.add(getRandomValFromArray(stringRange.value()));
                injectNotEmpty = false;
            } else if (annotation instanceof NotEmpty) {
                if (field.getType().isAssignableFrom(List.class)) {
                    values.add(List.of(new Object()));
                } else if (field.getType().isAssignableFrom(Set.class)) {
                    values.add(Set.of(new Object()));
                } else if (field.getType().isAssignableFrom(String.class)) {
                    values.add(UUID.randomUUID().toString());
                } else {
                    if (injectNotEmpty) {
                        values.add(new Object());
                    }
                }
            }
        }
        return values;
    }

    private long getRandomValFromArray(long[] value) {
        int length = value.length;
        int index = RANDOM.nextInt(0, length - 1);
        return value[index];
    }

    private int getRandomValFromArray(int[] value) {
        int length = value.length;
        int index = RANDOM.nextInt(0, length - 1);
        return value[index];
    }

    private <T> T getRandomValFromArray(T[] value) {
        int length = value.length;
        int index = RANDOM.nextInt(0, length - 1);
        return value[index];
    }

    private String getValidForLength(Length length) {
        int equals = length.equals();
        if (equals != Integer.MIN_VALUE) {
            return stringOfLen(equals);
        }
        int max = length.max();
        int min = length.min();
        if (max != Integer.MAX_VALUE && min != Integer.MIN_VALUE) {
            return stringOfLen(RANDOM.nextInt(min, max));
        } else if (min != Integer.MIN_VALUE) {
            return stringOfLen(min + 1);
        }
        return stringOfLen(max - 1);
    }

    private Object getValidForBound(Field field, Annotation annotation) {
        Bound bound = new BoundExtractor().extract(annotation);

        NullValueStrategy nullValueStrategy = bound.nullValueStrategy;
        if (!field.getType().isPrimitive()) {
            if (nullValueStrategy.equals(NullValueStrategy.NULL_ALLOWED)) {
                int rand = RANDOM.nextInt(10);
                if (rand % 2 == 0) {
                    return null;
                }
            }
        }
        long equals = bound.equals;
        if (equals != bound.minBound) {
            return bound.valueConverter.apply(equals);
        } else {
            long min = bound.min;
            long max = bound.max;
            if (min != bound.minBound && max != bound.maxBound) {
                return randomNumber(min, max, bound.valueConverter);
            } else if (min != bound.minBound) {
                return randomNumber(min, bound.maxBound, bound.valueConverter);
            }
            return randomNumber(bound.minBound, max, bound.valueConverter);
        }
    }

    public Object randomOutOfBounds(long min, long max,
                                    long minRange, long maxRange,
                                    Function<Long, Object> converter) {
        long minOffRange = RANDOM.nextLong(minRange, min - 1);
        long maxOffRange = RANDOM.nextLong(max + 1, maxRange);
        int index = RANDOM.nextInt(0, 1);
        if (index == 0) {
            return converter.apply(minOffRange);
        }
        return converter.apply(maxOffRange);
    }

    public Object randomNumber(long min, long max, Function<Long, Object> converter) {
        long randNumber = RANDOM.nextLong(min, max);
        return converter.apply(randNumber);
    }

    private void setValue(Field field, Object value, Object target) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType().isPrimitive()) {
            if (value instanceof Integer) {
                field.setInt(target, (Integer) value);
            } else if (value instanceof Short) {
                field.setShort(target, (Short) value);
            } else if (value instanceof Byte) {
                field.setByte(target, (Byte) value);
            } else if (value instanceof Long) {
                field.setLong(target, (Long) value);
            } else if (value instanceof Character) {
                field.setChar(target, (Character) value);
            } else if (value instanceof Boolean) {
                field.setBoolean(target, (Boolean) value);
            } else if (value instanceof Float) {
                field.setFloat(target, (Float) value);
            } else if (value instanceof Double) {
                field.setDouble(target, (Double) value);
            }
        } else {
            field.set(target, value);
        }
    }

    class BoundExtractor {

        public Bound extract(Annotation annotation) {
            if (annotation instanceof LongBound) {
                LongBound longBound = (LongBound) annotation;
                return new Bound(longBound.nullValueStrategy(), longBound.min(), longBound.max(),
                        longBound.equals(), Long.MIN_VALUE, Long.MAX_VALUE, Long::longValue);
            } else if (annotation instanceof IntBound) {
                IntBound intBound = (IntBound) annotation;
                return new Bound(intBound.nullValueStrategy(), intBound.min(), intBound.max(),
                        intBound.equals(), Integer.MIN_VALUE, Integer.MAX_VALUE, Long::intValue);
            } else if (annotation instanceof ShortBound) {
                ShortBound shortBound = (ShortBound) annotation;
                return new Bound(shortBound.nullValueStrategy(), shortBound.min(), shortBound.max(),
                        shortBound.equals(), Short.MIN_VALUE, Short.MAX_VALUE, Long::shortValue);
            } else if (annotation instanceof ByteBound) {
                ByteBound byteBound = (ByteBound) annotation;
                return new Bound(byteBound.nullValueStrategy(), byteBound.min(), byteBound.max(),
                        byteBound.equals(), Byte.MIN_VALUE, Byte.MAX_VALUE, Long::byteValue);
            }
            throw new IllegalArgumentException(annotation + " is not bound");
        }

    }

    @AllArgsConstructor
    class Bound {
        NullValueStrategy nullValueStrategy;
        long min;
        long max;
        long equals;
        long minBound;
        long maxBound;
        Function<Long, Object> valueConverter;
    }

}
