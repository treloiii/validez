package validez.lib.api.external;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Class which implements annotation properties and they value functionality.
 * Object of this class will be generated during annotation processing compilation stage.
 */
@RequiredArgsConstructor
public class AnnotationProperties {

    private final Map<String, Object> properties;

    /**
     * Retrieves value from associated property
     * @param property annotation property
     * @return value of annotation property.
     * the values will be of the same types that can be specified in the
     * annotations, except for the annotations themselves,
     * which will be replaced by {@link AnnotationProperties} object
     * @param <T> property type
     */
    @Nullable
    public <T> T getValue(String property) {
        Object value = properties.get(property);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

}
