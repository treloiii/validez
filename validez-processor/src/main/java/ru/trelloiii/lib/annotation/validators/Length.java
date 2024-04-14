package ru.trelloiii.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Length {

    int min() default DEFAULT;

    int max() default DEFAULT;

    int equals() default DEFAULT;

    int DEFAULT = -1;
}
