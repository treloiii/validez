package validez.lib.api.messaging;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Interface provide API for handling exception messages,
 * which will be thrown during validation by {@link validez.lib.annotation.Validator}
 * <br>
 * Implementation class must be declared through
 * {@link validez.lib.annotation.messaging.ModifyMessage} annotation API.
 * <br>
 * Implementor object will be allocated on each object validation,
 * so, recommended not make 'fat' implementation for less garbage producing.
 */
public interface MessageHandler {

    /**
     * Method will be called, when field invalid, just before exception throwing, for creating exception message
     * @param fieldName invalid field name
     * @param context validation context
     * @see ValidatorContext
     * @return message for exception
     */
    @Nonnull
    String handle(@Nonnull String fieldName, @Nonnull ValidatorContext context);

    /**
     * Method will be called, when invariant invalid, just before exception throwing, for creating exception message
     * @param invariantName not passed invariant name
     * @param membersContext validation context's of all invalid invariant fields, each entry of Map is not null.
     * @see ValidatorContext
     * @return message for exception
     */
    @Nonnull
    String handleInvariant(@Nonnull String invariantName, @Nonnull Map<String, ValidatorContext> membersContext);

    /**
     * Method will be called, if passed to validator {@link validez.lib.annotation.Validator} object is null
     * @return message for exception
     */
    String handleNull();
}
