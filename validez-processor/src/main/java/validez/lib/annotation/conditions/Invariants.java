package validez.lib.annotation.conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Invariants {

    Invariant[] value();

}
