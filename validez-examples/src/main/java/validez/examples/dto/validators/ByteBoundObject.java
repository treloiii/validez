package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.ByteBound;

@Data
@Validate
public class ByteBoundObject {

    @ByteBound(min = 118)
    private byte byteMinVal;
    @ByteBound(min = 111)
    private Byte byteBoxedMinVal;

    @ByteBound(max = 30)
    private byte byteMaxVal;
    @ByteBound(max = 15)
    private Byte byteBoxedMaxVal;

    @ByteBound(equals = 123)
    private byte byteEqualsVal;
    @ByteBound(equals = 3)
    private Byte byteBoxedEqualsVal;

    @ByteBound(min = 9, max = 54)
    private byte byteBoundVal;
    @ByteBound(min = 1, max = 34)
    private Byte byteBoxedBoundVal;

}
