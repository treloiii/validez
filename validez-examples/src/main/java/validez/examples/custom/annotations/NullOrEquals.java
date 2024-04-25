package validez.examples.custom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface NullOrEquals {

    int eqInt() default -1;

}
