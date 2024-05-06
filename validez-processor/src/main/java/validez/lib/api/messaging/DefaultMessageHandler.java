package validez.lib.api.messaging;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * This handler will be used by default if no ones is explicitly defined
 */
public class DefaultMessageHandler implements MessageHandler {

    @Nonnull
    @Override
    public String handle(ValidatorContext context) {
        String fieldName = context.getFieldName();
        return "%s field is not valid".formatted(fieldName);
    }

    @Override
    @Nonnull
    public String handleInvariant(@Nonnull String invariantName, @Nonnull Map<String, ValidatorContext> membersContext) {
        return "Invariant %s is not valid".formatted(invariantName);
    }

    @Override
    public String handleNull() {
        return "Object is not valid, because it is null";
    }

}
