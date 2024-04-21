package validez.lib.api.messaging;

public interface MessageHandler {

    String handle(String fieldName, ValidatorContext context);

}
