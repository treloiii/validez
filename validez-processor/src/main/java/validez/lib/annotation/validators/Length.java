package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check what string or CharSequence in specified length.
 * <br>
 * Usage examples:
 *
 * <pre>{@code
 *
 * //check if string.length() > 3
 * @Length(min = 3)
 * private String string;
 *
 * //check if string.length() < 32
 * @Length(max = 32)
 * private String string;
 *
 * //check if string.length() > 3 and < 99
 * @Length(min = 3, max = 99)
 * private String string;
 *
 * //check if string.length() = 12
 * @Length(equals = 12)
 * private String string;
 *
 * }</pre>
 */
@Target(ElementType.FIELD)
@Consumes(CharSequence.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Length {

    /**
     *
     * @return Lower bound for length
     */
    int min() default Integer.MIN_VALUE;

    /**
     *
     * @return Upper bound for length
     */
    int max() default Integer.MAX_VALUE;

    /**
     * @return Equals bound for value
     * @apiNote if used, when {@link Length#max()} and {@link Length#min()} will be ignored
     */
    int equals() default Integer.MIN_VALUE;

    /**
     * Define strategy which must be used, when field type is not primitive and its value = null
     * @see NullValueStrategy
     * @return null value handling strategy
     */
    NullValueStrategy nullValueStrategy() default NullValueStrategy.NULL_NOT_ALLOWED;

}
