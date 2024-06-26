package validez.examples.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Exclude;
import validez.lib.annotation.conditions.Partial;
import validez.lib.annotation.validators.IntBound;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NotNull;
import validez.lib.annotation.validators.StringRange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Validate
public class PersonInformation {

    @Length(max = 32, min = 5)
    private String name;
    @Length(max = 32, min = 3)
    private String surname;
    @Length(equals = 16)
    private String inn;
    @NotEmpty
    @IntBound(min = 18, max = 64)
    private Integer age;
    @IntBound(min = 1982, max = 2016)
    private int born;
    @NotEmpty
    private List<PersonInformation> children;
    @NotEmpty
    private Set<String> addressLines;
    @NotEmpty
    @Length(min = 3)
    private String pseudonym;
    @StringRange(value = {"ST1", "ST2"})
    private String status;
    @LongRange({123L, 1444L, 893L})
    private Long longStatus;
    @IntRange({1, 58, 99, 134})
    private Integer intStatus;
    private PaymentData paymentData;
    @Exclude
    private PaymentData excludedPaymentData;
    @Partial(include = {"phoneNumber", "pam"})
    private PaymentData partialPaymentData;
    @Partial(exclude = {"pam"})
    private PaymentData partialExcludePaymentData;

    @NotNull
    private BigDecimal decimalVal;
}
