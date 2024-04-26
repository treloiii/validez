package validez.lib.annotation;

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

}
