package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check short or Short value,
 * which must be in defined bound
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //checks that val > 30 && val < 134
 * @ShortBound(min = 30, max = 134)
 * short shortVal;
 * //checks that val > 30
 * @ShortBound(min = 30)
 * short shortVal;
 * //checks that val < 134
 * @ShortBound(max = 134)
 * short shortVal;
 * //checks that val == 99
 * @ShortBound(equals = 99)
 * short shortVal;
 * //checks that val == 100 && val != null
 * @ShortBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_NOT_ALLOWED)
 * Short boxedShort;
 * //checks that val == 100 || val == null
 * @ShortBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_ALLOWED)
 * Short boxedShort;
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Consumes(Short.class)
public @interface ShortBound {

    /**
     * @return Lower bound for value
     */
    short min() default Short.MIN_VALUE;

    /**
     * @return Upper bound for value
     */
    short max() default Short.MAX_VALUE;

    /**
     * @return Equals bound for value
     * @apiNote if used, when {@link ShortBound#max()} and {@link ShortBound#min()} will be ignored
     */
    short equals() default Short.MIN_VALUE;

    /**
     * Define strategy which must be used, when field type is not primitive and its value = null
     * @see NullValueStrategy
     * @return null value handling strategy
     */
    NullValueStrategy nullValueStrategy() default NullValueStrategy.NULL_NOT_ALLOWED;

}
