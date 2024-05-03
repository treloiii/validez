package validez.lib.api.messaging;

import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.StringRange;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * This handler will be used by default if no ones is explicitly defined
 */
public class DefaultMessageHandler implements MessageHandler {

    private static final Map<Class<? extends Annotation>, String> PATTERNS =
            Map.of(
                    IntRange.class, "%s not in integer range",
                    Length.class, "%s not valid length",
                    LongRange.class, "%s not in long range",
                    NotEmpty.class, "%s is null or empty",
                    StringRange.class, "%s not in string range"
            );

    @Nonnull
    @Override
    public String handle(String fieldName, ValidatorContext context) {
        Class<? extends Annotation> annotation = context.getAnnotationClass();
        if (annotation == null) {
            //only with caught exception
            Exception cause = context.getCause();
            if (cause == null) {
                //not possible, returns simple message
                return "%s is invalid".formatted(fieldName);
            }
            return cause.getMessage();
        }
        String pattern = PATTERNS.get(annotation);
        if (pattern == null) {
            return "%s is invalid by %s".formatted(fieldName, context.getFieldName());
        }
        return pattern.formatted(fieldName);
    }

    @Override
    @Nonnull
    public String handleInvariant(@Nonnull String invariantName, @Nonnull Map<String, ValidatorContext> membersContext) {
        return "invariant %s".formatted(invariantName);
    }

    @Override
    public String handleNull() {
        return "argument is null";
    }

}
