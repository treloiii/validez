package validez.lib.api;

import validez.lib.exceptions.InvalidException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class provide API for validates objects using its classes.
 * You can use this class to validate objects instead of explicitly allocate {@link Validator} objects.
 * @apiNote If object which need to validate possible null, use {@link Validators#forClass(Class, Class)}
 * method for extract validator, which will null check object inside.
 */
@SuppressWarnings("unchecked")
public class Validators {

    private Validators() {
    }

    static final Map<Class<?>, Validator<?, ?>> validators = new HashMap<>();

    static {
        try {
            Class.forName("validez.lib.api.ValidatorsFiller");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves validator for specified object class
     * @param targetClass object class for which validator will be generated
     * @param exception exception custom exception class which will be thrown,
     * @return validator
     * @param <T> object type
     * @param <E> custom exception class defined by {@link validez.lib.annotation.ValidatorThrows}
     * @throws IllegalArgumentException if no validator present for specified class
     * @throws NullPointerException if targetClass is null
     */
    public static <T, E extends Exception> Validator<T, E> forClass(Class<T> targetClass, Class<E> exception) {
        checkNull(targetClass, "targetClass");
        Validator<T, E> validator = (Validator<T, E>) validators.get(targetClass);
        if (validator == null) {
            throw new IllegalArgumentException("No validator registered for class "
                    + targetClass.getCanonicalName());
        }
        return validator;
    }

    /**
     * Validate object
     * @param target object which need validate
     * @param exception custom exception class which will be thrown
     * @param <T> object type
     * @param <E> type of custom exception class
     * @throws E custom exception class defined by {@link validez.lib.annotation.ValidatorThrows}
     * @throws NullPointerException if target is null
     */
    public static <T, E extends Exception> void validate(T target, Class<E> exception) throws E {
        checkNull(target, "target");
        Validator<T, E> validator = (Validator<T, E>) forClass(target.getClass(), exception);
        validator.validate(target, null, null);
    }

    /**
     * Validate object
     * @param target object which need validate
     * @param <T> object type
     * @throws InvalidException default exception if specified not provided
     * @throws NullPointerException if target is null
     */
    public static <T> void validate(T target) throws InvalidException {
        checkNull(target, "target");
        Validator<T, InvalidException> validator = (Validator<T, InvalidException>)
                forClass(target.getClass(), InvalidException.class);
        validator.validate(target, null, null);
    }

    /**
     * Validate object using only specified fields
     * @param target object which need validate
     * @param includes list of fields which must be used for validation
     * @param <T> object type
     * @throws InvalidException default exception if specified not provided
     * @throws NullPointerException if target is null
     */
    public static <T> void validateOnly(T target, String... includes) throws InvalidException {
        checkNull(target, "target");
        Validator<T, InvalidException> validator = (Validator<T, InvalidException>)
                forClass(target.getClass(), InvalidException.class);
        Set<String> includesSet = includes.length == 0 ? Collections.emptySet() : Set.of(includes);
        validator.validate(target, includesSet, null);
    }

    /**
     * Validate object without specified fields
     * @param target object which need validate
     * @param excludes list of fields which must be excluded from validation
     * @param <T> object type
     * @throws InvalidException default exception if specified not provided
     * @throws NullPointerException if target is null
     */
    public static <T> void validateWithout(T target, String... excludes) throws InvalidException {
        checkNull(target, "target");
        Validator<T, InvalidException> validator = (Validator<T, InvalidException>)
                forClass(target.getClass(), InvalidException.class);
        Set<String> excludesSet = excludes.length == 0 ? Collections.emptySet() : Set.of(excludes);
        validator.validate(target, null, excludesSet);
    }

    private static void checkNull(Object check, String name) {
        if (check == null) {
            throw new NullPointerException("%s cannot be null".formatted(name));
        }
    }

}
