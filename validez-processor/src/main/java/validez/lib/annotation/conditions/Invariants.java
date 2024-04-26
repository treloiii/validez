package validez.lib.annotation.conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation used for grouping {@link Invariant} annotations together on class
 */
@Target(ElementType.TYPE)
public @interface Invariants {

    /**
     *
     * @return Array of {@link Invariant}
     */
    Invariant[] value();

}
