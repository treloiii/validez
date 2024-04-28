package validez.examples.handler;

import validez.lib.api.messaging.MessageHandler;
import validez.lib.api.messaging.ValidatorContext;

import java.util.Map;

public class CustomMessageHandler implements MessageHandler {

    public static Boolean nullHandled;
    public static ValidatorContext handledContext;
    public static Map<String, ValidatorContext> handledInvariant;

    @Override
    public String handle(String fieldName, ValidatorContext context) {
        handledContext = context;
        return "%s field name not valid, %s value = ".formatted(fieldName, fieldName)
                + context.getFieldValue();
    }

    @Override
    public String handleInvariant(String invariantName, Map<String, ValidatorContext> membersContext) {
        handledInvariant = membersContext;
        return "invariant %s not passed".formatted(invariantName);
    }

    @Override
    public String handleNull() {
        nullHandled = true;
        return "null object provided";
    }
}
