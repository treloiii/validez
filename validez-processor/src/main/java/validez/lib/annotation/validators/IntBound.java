package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check int or Integer value,
 * which must be in defined bound
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //checks that val > 30 && val < 134
 * @IntBound(min = 30, max = 134)
 * int intVal;
 * //checks that val > 30
 * @IntBound(min = 30)
 * int intVal;
 * //checks that val < 134
 * @IntBound(max = 134)
 * int intVal;
 * //checks that val == 99
 * @IntBound(equals = 99)
 * int intVal;
 * //checks that val == 100 && val != null
 * @IntBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_NOT_ALLOWED)
 * Integer boxedInteger;
 * //checks that val == 100 || val == null
 * @IntBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_ALLOWED)
 * Integer boxedInteger;
 * }</pre>
 */
@Target(ElementType.FIELD)
@Consumes(Integer.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntBound {

    /**
     * @return Lower bound for value
     */
    int min() default Integer.MIN_VALUE;

    /**
     * @return Upper bound for value
     */
    int max() default Integer.MAX_VALUE;

    /**
     * @return Equals bound for value
     * @apiNote if used, when {@link IntBound#max()} and {@link IntBound#min()} will be ignored
     */
    int equals() default Integer.MIN_VALUE;

    /**
     * Define strategy which must be used, when field type is not primitive and its value = null
     * @see NullValueStrategy
     * @return null value handling strategy
     */
    NullValueStrategy nullValueStrategy() default NullValueStrategy.NULL_NOT_ALLOWED;

}
