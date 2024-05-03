package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.ShortBound;

@Data
@Validate
public class ShortBoundObject {

    @ShortBound(min = 10045)
    private short shortMinVal;
    @ShortBound(min = 15023)
    private Short shortBoxedMinVal;

    @ShortBound(max = 30)
    private short shortMaxVal;
    @ShortBound(max = 234)
    private Short shortBoxedMaxVal;

    @ShortBound(equals = 123)
    private short shortEqualsVal;
    @ShortBound(equals = 4235)
    private Short shortBoxedEqualsVal;

    @ShortBound(min = 12, max = 34)
    private short shortBoundVal;
    @ShortBound(min = 122, max = 453)
    private Short shortBoxedBoundVal;

}
