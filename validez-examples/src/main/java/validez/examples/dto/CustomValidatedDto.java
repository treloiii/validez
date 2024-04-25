package validez.examples.dto;

import lombok.Data;
import validez.examples.custom.annotations.AllPossibleTypes;
import validez.examples.custom.annotations.NullOrEquals;
import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Fields;
import validez.lib.annotation.conditions.Invariant;
import validez.lib.annotation.validators.NotEmpty;

@Data
@Validate
@Invariant(
        name = "first",
        members = {
                @Fields({"intField", "doubleVal"}),
                @Fields("stringVal")
        }
)
public class CustomValidatedDto {

    @NullOrEquals(eqInt = 152)
    private Integer intField;
    @AllPossibleTypes
    private double doubleVal;

    @NotEmpty
    private String stringVal;

    @NotEmpty
    private String stringVal2;
}
