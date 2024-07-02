package validez.examples.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Fields;
import validez.lib.annotation.conditions.Invariant;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validate
@Invariant(
        name = "recipientInfo",
        members = {
                @Fields({"phoneNumber", "pam"}),
                @Fields("accountNumber"),
                @Fields("cardNumber")
        }
)
@Invariant(
        name = "transactionData",
        members = {
                @Fields("qrCode"),
                @Fields("token")
        }
)
//take no effect
@Invariant(
        name = "paymentType",
        members = {
                @Fields("paymentType")
        }
)
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

    @NotEmpty
    @Length(equals = 16)
    private String cardNumber;

    @NotEmpty
    @Length(equals = 16)
    private String qrCode;

    @NotEmpty
    @Length(equals = 48)
    private String token;

    @IntRange({1, 2, 3})
    private int paymentType;

    @NotNull
    private String additionalInfo;
}
