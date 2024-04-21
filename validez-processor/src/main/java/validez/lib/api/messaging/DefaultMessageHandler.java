package validez.lib.api.messaging;

public class DefaultMessageHandler implements MessageHandler {

    @Override
    public String handle(String fieldName, ValidatorContext context) {
        return "%s not valid by %s".formatted(fieldName, context.getName());
    }

}
