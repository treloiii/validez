package validez.lib.annotation.conditions;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(Invariants.class)
public @interface Invariant {

    String name();

    Fields[] members();

}
