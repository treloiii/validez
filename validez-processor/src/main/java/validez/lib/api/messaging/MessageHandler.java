package validez.lib.api.messaging;

import javax.annotation.Nonnull;
import java.util.Map;

public interface MessageHandler {

    @Nonnull
    String handle(@Nonnull String fieldName, @Nonnull ValidatorContext context);

    @Nonnull
    String handleInvariant(@Nonnull String invariantName, @Nonnull Map<String, ValidatorContext> membersContext);

}
