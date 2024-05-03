package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check if int or Integer field
 * contains in specified in {@link IntRange#value()} array
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //check if intField value contains in [1, 44, 238, 543]
 * @IntRange(value = {1, 44, 238, 543})
 * private int intField;
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Consumes(Integer.class)
public @interface IntRange {

    /**
     * @return array of ints where field value must be present
     */
    int[] value();

}
