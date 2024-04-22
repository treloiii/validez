package validez.lib.api.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

@AllArgsConstructor
@Getter
@ToString
public class ValidatorContext {

    private final String name;
    private final Class<? extends Annotation> annotationClass;
    @Nullable
    private final String property;
    @Nullable
    private final Object fieldValue;

}
