package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check if field value != null
 * <br>
 * Usage examples:
 * <pre>{@code
 * //will check if field != null
 * @NotEmpty
 * private Object field;
 * }</pre>
 * <br>
 * Will be ignored if used with primitive type
 */
@Target(ElementType.FIELD)
@Consumes(Object.class)
public @interface NotNull {
}
