package validez.lib.annotation.conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker for compile time excluding field from validation
 */
@Target(ElementType.FIELD)
public @interface Exclude {
}
