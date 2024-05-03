package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.IntBound;

@Validate
public class IntBoundAppliedNotToInteger {

    @IntBound(equals = 12)
    private Object bound;

    public Object getBound() {
        return bound;
    }
}