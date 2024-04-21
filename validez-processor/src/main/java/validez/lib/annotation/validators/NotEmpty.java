package validez.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface NotEmpty {

    String group() default "";

    String message() default "$N can not be empty";

    boolean format() default true;

}
