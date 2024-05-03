package validez.lib.annotation;

import validez.lib.api.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation provides API for customizing exception class, which must be thrown by {@link Validator} object
 * <br>
 * This exception will be substituted to {@link Validator} during annotation processing in compile-time.
 * Use this annotation on class which marked as {@link Validate} for providing exception.
 * <br>
 * Usage example:
 *
 * <pre>{@code
 *
 * public class CarInvalidException extends Exception {
 *     public CarInvalidException(String message) {
 *         super(message);
 *     }
 * }
 *
 * @Validate
 * @ValidatorThrows(CarInvalidException.class)
 * public class Car {
 *     ...
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
public @interface ValidatorThrows {

    /**
     * When providing exception class argument, be sure that the class meets the following requirements:
     * <ul>
     *     <li>class is public</li>
     *     <li>class have public constructor with one {@link String} argument</li>
     * </ul>
     * These requirements exist because processor tries to allocate new exception, with message from
     * {@link validez.lib.api.messaging.MessageHandler}
     * <br>
     * Exception class can be both checked and unchecked
     * @return exception class which must be thrown by validator
     */
    Class<? extends Exception> value();

}
