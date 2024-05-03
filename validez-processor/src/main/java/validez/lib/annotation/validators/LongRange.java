package validez.lib.annotation.validators;

import validez.lib.annotation.internal.Consumes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * One of the in-box validator, used for check if long or Long field
 * contains in specified in {@link LongRange#value()} array
 * <br>
 *
 * Usage example:
 * <pre>{@code
 * //check if longField value contains in [213L, 23333L, 93249L, 89L]
 * @IntRange(value = {213L, 23333L, 93249L, 89L})
 * private long longField;
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Consumes(Long.class)
public @interface LongRange {

    /**
     *
     * @return array of longs where field value must be present
     */
    long[] value();

}
