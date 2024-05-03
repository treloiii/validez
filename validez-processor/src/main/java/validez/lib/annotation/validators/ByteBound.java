package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check byte or Byte value,
 * which must be in defined bound
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //checks that val > 30 && val < 134
 * @ByteBound(min = 30, max = 134)
 * byte byteVal;
 * //checks that val > 30
 * @ByteBound(min = 30)
 * byte byteVal;
 * //checks that val < 134
 * @ByteBound(max = 134)
 * byte byteVal;
 * //checks that val == 99
 * @ByteBound(equals = 99)
 * byte byteVal;
 * //checks that val == 100 && val != null
 * @ByteBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_NOT_ALLOWED)
 * Byte boxedByte;
 * //checks that val == 100 || val == null
 * @ByteBound(equals = 100, nullValueStrategy = NullValueStrategy.NULL_ALLOWED)
 * Byte boxedByte;
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Consumes(Byte.class)
public @interface ByteBound {

    /**
     * @return Lower bound for value
     */
    byte min() default Byte.MIN_VALUE;

    /**
     * @return Upper bound for value
     */
    byte max() default Byte.MAX_VALUE;

    /**
     * @return Equals bound for value
     * @apiNote if used, when {@link ByteBound#max()} and {@link ByteBound#min()} will be ignored
     */
    byte equals() default Byte.MIN_VALUE;

    /**
     * Define strategy which must be used, when field type is not primitive and its value = null
     * @see NullValueStrategy
     * @return null value handling strategy
     */
    NullValueStrategy nullValueStrategy() default NullValueStrategy.NULL_NOT_ALLOWED;

}
