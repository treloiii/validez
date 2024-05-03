package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.LongBound;

@Data
@Validate
public class LongBoundObject {

    @LongBound(min = 10045)
    private long longMinVal;
    @LongBound(min = 15023)
    private Long longBoxedMinVal;

    @LongBound(max = 30012123123123L)
    private long longMaxVal;
    @LongBound(max = 35043123123213L)
    private Long longBoxedMaxVal;

    @LongBound(equals = 49032)
    private long longEqualsVal;
    @LongBound(equals = 77712)
    private Long longBoxedEqualsVal;

    @LongBound(min = 90434, max = 2002434)
    private long longBoundVal;
    @LongBound(min = 32323, max = 239032435)
    private Long longBoxedBoundVal;

}
