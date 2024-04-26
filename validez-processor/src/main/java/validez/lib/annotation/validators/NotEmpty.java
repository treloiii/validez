package validez.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check if field value != null and not empty
 * on {@link String} or {@link java.util.Collection} and it subtypes via .isEmpty() call
 * <br>
 * Usage examples:
 * <pre>{@code
 * //will check if field != null
 * @NotEmpty
 * private Object field;
 * //will check if stringValue != null and !stringValue.isEmpty()
 * @NotEmpty
 * private String stringValue;
 * //will check if collection != null and !collection.isEmpty()
 * @NotEmpty
 * private Collection<?> collection
 * }</pre>
 *
 */
@Target(ElementType.FIELD)
public @interface NotEmpty {
}
