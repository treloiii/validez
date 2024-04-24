package validez.examples.handler;

import validez.lib.api.messaging.MessageHandler;
import validez.lib.api.messaging.ValidatorContext;

import java.util.Map;

public class CustomMessageHandler implements MessageHandler {
    @Override
    public String handle(String fieldName, ValidatorContext context) {
        return "%s field name not valid, %s value = ".formatted(fieldName, fieldName)
                + context.getFieldValue();
    }

    @Override
    public String handleInvariant(String invariantName, Map<String, ValidatorContext> membersContext) {
        return "invariant %s not passed".formatted(invariantName);
    }
}
