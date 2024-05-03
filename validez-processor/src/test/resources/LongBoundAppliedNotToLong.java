package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.LongBound;

@Validate
public class LongBoundAppliedNotToLong {

    @LongBound(min = 30)
    private Object bound;

    public Object getBound() {
        return bound;
    }
}