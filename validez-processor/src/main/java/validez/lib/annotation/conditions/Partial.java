package validez.lib.annotation.conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Applies to complex type which marked as {@link validez.lib.annotation.Validate}
 * for excluding or including specified fields of current field from validation
 */
@Target(ElementType.FIELD)
public @interface Partial {


    String[] include() default {};

    String[] exclude() default {};

}
