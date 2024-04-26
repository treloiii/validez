package validez.lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark class for which you need implement validation object generation.
 * <br>
 * Next on marked class fields you can use some default validators
 * from <br>{@link validez.lib.annotation.validators} package
 * or your own custom validators using <br> {@link validez.lib.api.external.ExternalValidator}
 * <br>
 * <br>
 * For all fields, which are part of validation (marked with the appropriate annotations),
 * must exist getter method (lombok supported), otherwise compilation will fail,
 * because processor tries access field values through public not static getter methods
 * named according to java naming convention
 * <br>
 * <br>
 * @apiNote Records currently not supported
 */
@Target(ElementType.TYPE)
public @interface Validate {
}
