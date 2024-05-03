package validez.lib.api;

import validez.lib.annotation.ValidatorThrows;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Processor will generate classes which implements this interface for validation functionality.
 * You can explicitly use this classes via
 * <pre>{@code new ValidatorImpl().validate(...)}</pre>
 * or by using API from class {@link validez.lib.api.Validators}
 * @param <T> Type for object which should validate
 * @param <E> Type for exception which should throws when object is not valid
 * @see ValidatorThrows
 */
public interface Validator<T, E extends Exception> {

    /**
     * Validation method. Call this method for validate object.
     * @param object object which should validate
     * @param includes list of field, which need to validate ONLY.
     *                <br>
     *                 If provided, validation process will use only this fields for validating object
     * @param excludes list of field, which must be excluded from validation process
     * @throws E when object is not valid
     */
    void validate(T object, @Nullable Set<String> includes, @Nullable Set<String> excludes) throws E;

    /**
     * Validation method, which delegates to originally validate with null includes and excludes.
     * @param object object which should validate
     * @throws E when object is not valid
     */
    default void validate(T object) throws E {
        this.validate(object, null, null);
    }

    /**
     * Validation method, which delegates to originally validate with provided includes and null excludes.
     * Use this method for validate only listed in includes fields.
     * @param object object which should validate
     * @throws E when object is not valid
     */
    default void validateIncludes(T object, Set<String> includes) throws E {
        this.validate(object, includes, null);
    }

    /**
     * Validation method, which delegates to originally validate with null includes and provided excludes.
     * Use this method for validate all fields except of listed in excludes.
     * @param object object which should validate
     * @throws E when object is not valid
     */
    default void validateExcludes(T object, Set<String> excludes) throws E {
        this.validate(object, null, excludes);
    }

}
