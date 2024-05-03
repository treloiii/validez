package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.LongRange;

@Validate
public class LongRangeAppliedNotToLong {

    @LongRange({123, 124, 234234L})
    private String string;

    public String getString() {
        return string;
    }

}