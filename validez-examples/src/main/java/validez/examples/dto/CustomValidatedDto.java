package validez.examples.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import validez.examples.custom.annotations.AllPossibleTypes;
import validez.examples.custom.annotations.NullOrEquals;
import validez.examples.handler.CustomMessageHandler;
import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Fields;
import validez.lib.annotation.conditions.Invariant;
import validez.lib.annotation.messaging.ModifyMessage;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validate
@Invariant(
        name = "first",
        members = {
                @Fields({"intField", "doubleVal"}),
                @Fields("stringVal")
        }
)
@ModifyMessage(messageHandler = CustomMessageHandler.class)
public class CustomValidatedDto {

    @NullOrEquals(eqInt = 152)
    private Integer intField;
    @AllPossibleTypes
    private double doubleVal;
    @NotEmpty
    private String stringVal;
    @NotNull
    private String stringVal2;

}
