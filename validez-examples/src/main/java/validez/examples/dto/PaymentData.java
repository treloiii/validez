package validez.examples.dto;

import lombok.Data;
import validez.examples.handler.CustomMessageHandler;
import validez.lib.annotation.Validate;
import validez.lib.annotation.messaging.ModifyMessage;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.NotEmpty;

@Data
@Validate
@ModifyMessage(messageHandler = CustomMessageHandler.class)
public class PaymentData {

    @NotEmpty
    @Length(equals = 32)
    private String accountNumber;
    @NotEmpty
    @Length(equals = 11)
    private String phoneNumber;
    @NotEmpty
    @Length(min = 3)
    private String pam;

}
