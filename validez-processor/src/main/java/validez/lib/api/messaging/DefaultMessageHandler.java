package validez.lib.api.messaging;

import validez.lib.annotation.validators.*;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Map;

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
        String pattern = PATTERNS.get(annotation);
        if (pattern == null) {
            return "%s is not valid by %s".formatted(fieldName, context.getName());
        }
        return pattern.formatted(fieldName);
    }

}
