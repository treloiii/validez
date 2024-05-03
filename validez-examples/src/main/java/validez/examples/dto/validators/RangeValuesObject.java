package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.StringRange;

@Validate
@Data
public class RangeValuesObject {

    @StringRange({"A", "B", "C"})
    private String stringRange;
    @IntRange({11, 23, 4324, 432, 44})
    private int intRange;
    @IntRange({11, 23, 4324, 432, 44})
    private Integer intRangeBoxed;
    @LongRange({2134L, 4234234L, 2434234122313L})
    private long longRange;
    @LongRange({2134L, 4234234L, 2434234122313L})
    private Long longRangeBoxed;

}
