package validez.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check what string in specified length.
 * Also, can be used with Number objects for check it value bounds or equality
 * <br>
 * Usage examples:
 *
 * <pre>{@code
 *
 * //check if number > 3
 * @Length(min = 3)
 * private int number;
 *
 * //check if number < 32
 * @Length(max = 32)
 * private int number;
 *
 * //check if number > 3 and < 99
 * @Length(min = 3, max = 99)
 * private int number;
 *
 * //check if number = 12
 * @Length(equals = 12)
 * private int number;
 *
 * }</pre>
 *
 * For strings, behaviour will be same the difference is that the comparison will be made over
 * the value that the string returns as a result of the call String#lenght() on it
 */
@Target(ElementType.FIELD)
public @interface Length {

    /**
     *
     * @return lower bound for length
     */
    long min() default Long.MIN_VALUE;

    /**
     *
     * @return upper bound for length
     */
    long max() default Long.MAX_VALUE;

    /**
     * If used, then min and max conditions will be ignored
     * @return equality value
     */
    long equals() default Long.MIN_VALUE;

    /**
     * specifies what to do if field value equals null
     * @see NullValueStrategy
     * @return null value strategy
     */
    NullValueStrategy nullStrategy() default NullValueStrategy.NULL_ALLOWED;

}
