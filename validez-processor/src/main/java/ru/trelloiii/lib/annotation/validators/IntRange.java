package ru.trelloiii.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface IntRange {
    int[] value();
}
