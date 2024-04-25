package validez.lib.api.external;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;

@RequiredArgsConstructor
public class AnnotationProperties {

    private final Map<String, Object> properties;

    @Nullable
    public <T> T getValue(String property) {
        Object value = properties.get(property);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

}
