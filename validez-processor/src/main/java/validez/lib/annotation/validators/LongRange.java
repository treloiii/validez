package validez.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface LongRange {
    long[] value();

}
