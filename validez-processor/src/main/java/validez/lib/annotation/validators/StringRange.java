package validez.lib.annotation.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check if String field
 * contains in specified in {@link StringRange#value()} array
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //check if stringField value contains in ["one", "two", "none"]
 * @IntRange(value = {"one", "two", "none"})
 * private int stringField;
 * }</pre>
 */
@Target(ElementType.FIELD)
public @interface StringRange {

    /**
     *
     * @return array of strings where field value must be present
     */
    String[] value();

}
