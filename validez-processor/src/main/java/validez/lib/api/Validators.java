package validez.lib.api;

import validez.lib.api.data.ValidationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    static final Map<Class<?>, Validator<?>> validators = new HashMap<>();

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
     * @return validator
     * @param <T> object type
     * @throws IllegalArgumentException if no validator present for specified class
     * @throws NullPointerException if targetClass is null
     */
    public static <T> Validator<T> forClass(Class<T> targetClass) {
        checkNull(targetClass, "targetClass");
        Validator<T> validator = (Validator<T>) validators.get(targetClass);
        if (validator == null) {
            throw new IllegalArgumentException("No validator registered for class "
                    + targetClass.getCanonicalName());
        }
        return validator;
    }

    /**
     * Validate object
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
