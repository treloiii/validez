package validez.lib.annotation.messaging;

import validez.lib.api.messaging.MessageHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface ModifyMessage {

    Class<? extends MessageHandler> messageHandler();

}
