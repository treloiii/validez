package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.ByteBound;

@Validate
public class ByteBoundAppliedNotToByte {

    @ByteBound(max = 100)
    private Integer integer;

    public Integer getInteger() {
        return integer;
    }
}