package validez.lib.api.messaging;

import javax.annotation.Nonnull;

public interface MessageHandler {

    @Nonnull
    String handle(String fieldName, ValidatorContext context);

}
