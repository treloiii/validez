package validez.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Length {

    int min() default DEFAULT;

    int max() default DEFAULT;

    int equals() default DEFAULT;

    String group() default "";

    String message() default "Length of $N not valid";

    boolean format() default true;

    int DEFAULT = -1;
}
