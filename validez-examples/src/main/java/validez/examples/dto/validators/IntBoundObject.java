package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.IntBound;

@Data
@Validate
public class IntBoundObject {

    @IntBound(min = 100)
    private int intMinVal;
    @IntBound(min = 150)
    private Integer intBoxedMinVal;

    @IntBound(max = 300)
    private int intMaxVal;
    @IntBound(max = 350)
    private Integer intBoxedMaxVal;

    @IntBound(equals = 490)
    private int intEqualsVal;
    @IntBound(equals = 777)
    private Integer intBoxedEqualsVal;

    @IntBound(min = 90, max = 200)
    private int intBoundVal;
    @IntBound(min = 3, max = 239032)
    private Integer intBoxedBoundVal;
}
