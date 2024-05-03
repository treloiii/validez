package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.StringRange;

@Validate
public class StringRangeAppliedNotToString {

    @StringRange({"A"})
    private Object obj;

    public Object getObj() {
        return obj;
    }

}