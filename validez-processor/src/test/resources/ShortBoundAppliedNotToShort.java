package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.ShortBound;

@Validate
public class ShortBoundAppliedNotToShort {

    @ShortBound(equals = 12)
    private Object bound;

    public Object getBound() {
        return bound;
    }
}