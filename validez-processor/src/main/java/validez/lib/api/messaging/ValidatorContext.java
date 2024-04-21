package validez.lib.api.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ValidatorContext {

    private final String name;
    private final Class<?> annotationClass;
    private final String property;

}
