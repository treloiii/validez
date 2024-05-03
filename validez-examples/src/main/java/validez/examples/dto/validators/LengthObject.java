package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.Length;

@Data
@Validate
public class LengthObject {

    @Length(equals = 14)
    private String equals;
    @Length(min = 9)
    private String min;
    @Length(max = 15)
    private String max;
    @Length(min = 3, max = 9)
    private String bound;

}
