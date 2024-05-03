package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.IntRange;

@Validate
public class IntRangeAppliedNotToInt {

    @IntRange({123, 124, 4})
    private String string;

    public String getString() {
        return string;
    }

}