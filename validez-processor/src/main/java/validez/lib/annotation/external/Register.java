package validez.lib.annotation.external;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation used for register custom {@link validez.lib.api.external.ExternalValidator}<br>
 * If this annotation will be provided on class, that is not implements
 * {@link validez.lib.api.external.ExternalValidator}, then exception will be thrown
 */
@Target(ElementType.TYPE)
public @interface Register {
}
