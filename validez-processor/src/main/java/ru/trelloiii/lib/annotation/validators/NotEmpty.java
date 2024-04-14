package ru.trelloiii.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface NotEmpty {

    String message() default "$N can not be empty";

    boolean format() default true;

}
