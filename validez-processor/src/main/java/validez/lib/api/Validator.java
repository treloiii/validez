package validez.lib.api;

import validez.lib.api.data.ValidationResult;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Processor will generate classes which implements this interface for validation functionality.
 * You can explicitly use this classes via
 * <pre>{@code new ValidatorImpl().validate(...)}</pre>
 * or by using API from class {@link validez.lib.api.Validators}
 * @param <T> Type for object which should validate
 */
public interface Validator<T> {

    /**
     * Validation method. Call this method for validate object.
     * @param object object which should validate
     * @param includes list of field, which need to validate ONLY.
     *                <br>
     *                 If provided, validation process will use only this fields for validating object
     * @param excludes list of field, which must be excluded from validation process
     * @return result of validation {@link ValidationResult}
     */
    ValidationResult validate(T object, @Nullable Set<String> includes, @Nullable Set<String> excludes);

    /**
     * Validation method, which delegates to originally validate with null includes and excludes.
     * @param object object which should validate
     * @return result of validation {@link ValidationResult}
     */
    default ValidationResult validate(T object) {
        return this.validate(object, null, null);
    }

    /**
     * Validation method, which delegates to originally validate with provided includes and null excludes.
     * Use this method for validate only listed in includes fields.
     * @param object object which should validate
     * @return result of validation {@link ValidationResult}
     */
    default ValidationResult validateIncludes(T object, Set<String> includes) {
        return this.validate(object, includes, null);
    }

    /**
     * Validation method, which delegates to originally validate with null includes and provided excludes.
     * Use this method for validate all fields except of listed in excludes.
     * @param object object which should validate
     * @return result of validation {@link ValidationResult}
     */
    default ValidationResult validateExcludes(T object, Set<String> excludes) {
        return this.validate(object, null, excludes);
    }

}
