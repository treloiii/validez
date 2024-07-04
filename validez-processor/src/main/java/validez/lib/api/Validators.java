package validez.lib.api;

import validez.lib.api.data.ValidationResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;

/**
 * Class provide API for validates objects using its classes.
 * You can use this class to validate objects instead of explicitly allocate {@link Validator} objects.
 * @apiNote If object which need to validate possible null, use {@link Validators#forClass(Class)}
 * method for extract validator, which will null check object inside.
 */
@SuppressWarnings("unchecked")
public class Validators {

    private Validators() {
    }

    private static <T> Validator<T> loadValidator(Class<T> dtoClass) {
        try {
            Class<Validator<T>> validatorClass = (Class<Validator<T>>)
                    Class.forName(dtoClass.getCanonicalName() + "ValidatorImpl");
            Constructor<Validator<T>> constructor = validatorClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (ClassNotFoundException | InvocationTargetException |
                 NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("Cannot load and create instance of " +
                    " validator implementation for" + dtoClass.getName(), e);
        }
    }

    /**
     * Retrieves new validator for specified object class.
     * @param targetClass object class for which validator will be generated
     * @return validator
     * @param <T> object type
     * @throws IllegalArgumentException if no validator present for specified class
     * @throws NullPointerException if targetClass is null
     */
    public static <T> Validator<T> forClass(Class<T> targetClass) {
        checkNull(targetClass, "targetClass");
        return loadValidator(targetClass);
    }

    /**
     * Validate object.
     * new Validator will be created for validator
     * @param target object which need validate
     * @param <T> object type
     * @throws NullPointerException if target is null
     * @return result of validation {@link ValidationResult}
     */
    public static <T> ValidationResult validate(T target) {
        checkNull(target, "target");
        Validator<T> validator = (Validator<T>) forClass(target.getClass());
        return validator.validate(target, null, null);
    }

    /**
     * Validate object using only specified fields
     * new Validator will be created for validator
     * @param target object which need validate
     * @param includes list of fields which must be used for validation
     * @param <T> object type
     * @return result of validation {@link ValidationResult}
     */
    public static <T> ValidationResult validateIncludes(T target, String... includes) {
        checkNull(target, "target");
        Validator<T> validator = (Validator<T>) forClass(target.getClass());
        Set<String> includesSet = includes.length == 0 ? Collections.emptySet() : Set.of(includes);
        return validator.validate(target, includesSet, null);
    }

    /**
     * Validate object without specified fields
     * new Validator will be created for validator
     * @param target object which need validate
     * @param excludes list of fields which must be excluded from validation
     * @param <T> object type
     * @return result of validation {@link ValidationResult}
     */
    public static <T> ValidationResult validateExcludes(T target, String... excludes) {
        checkNull(target, "target");
        Validator<T> validator = (Validator<T>) forClass(target.getClass());
        Set<String> excludesSet = excludes.length == 0 ? Collections.emptySet() : Set.of(excludes);
        return validator.validate(target, null, excludesSet);
    }

    private static void checkNull(Object check, String name) {
        if (check == null) {
            throw new NullPointerException("%s cannot be null".formatted(name));
        }
    }

}
