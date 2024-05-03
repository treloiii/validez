package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.ByteBound;
import validez.lib.annotation.validators.IntBound;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.NotEmpty;

import java.util.List;

@Validate
@Data
public class MixedValidatorsObject {

    @IntBound(min = 90, max = 19900)
    @NotEmpty
    private Integer integer;
    @LongRange({99L, 1234123123123L, 4234234123123L})
    @NotEmpty
    private Long range;
    @ByteBound(equals = 13)
    private byte byteVal;
    @NotEmpty
    @Length(min = 30)
    private String string;
    @NotEmpty
    private List<Object> list;

}
