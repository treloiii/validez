package validez.lib.annotation.messaging;

import validez.lib.api.messaging.MessageHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation used to provide custom {@link MessageHandler} for modifying exception messages,
 * that will be thrown if object not pass validation.
 * If this annotation doesn't provide on class, then default {@link validez.lib.api.messaging.DefaultMessageHandler}
 * will be used
 */
@Target(ElementType.TYPE)
public @interface ModifyMessage {

    /**
     * Specifies class for custom exception message handling.
     * @return message handler class
     */
    Class<? extends MessageHandler> messageHandler();

}
