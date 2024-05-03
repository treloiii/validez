package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
@Retention(RetentionPolicy.RUNTIME)
@Consumes(Object.class)
public @interface NotEmpty {
}
