package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check long or Long value,
 * which must be in defined bound
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //checks that val > 30 && val < 134
 * @LongBound(min = 30, max = 134)
 * long longVal;
 * //checks that val > 30
 * @LongBound(min = 30)
 * long longVal;
 * //checks that val < 134
 * @LongBound(max = 134)
 * long longVal;
 * //checks that val == 99
 * @LongBound(equals = 99)
 * long longVal;
 * //checks that val == 100 && val != null
 * @LongBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_NOT_ALLOWED)
 * Long boxedLong;
 * //checks that val == 100 || val == null
 * @LongBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_ALLOWED)
 * Long boxedLong;
 * }</pre>
 */
@Consumes(Long.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LongBound {

    /**
     * @return Lower bound for value
     */
    long min() default Long.MIN_VALUE;

    /**
     * @return Upper bound for value
     */
    long max() default Long.MAX_VALUE;

    /**
     * @return Equals bound for value
     * @apiNote if used, when {@link LongBound#max()} and {@link LongBound#min()} will be ignored
     */
    long equals() default Long.MIN_VALUE;

    /**
     * Define strategy which must be used, when field type is not primitive and its value = null
     * @see NullValueStrategy
     * @return null value handling strategy
     */
    NullValueStrategy nullValueStrategy() default NullValueStrategy.NULL_NOT_ALLOWED;

}
