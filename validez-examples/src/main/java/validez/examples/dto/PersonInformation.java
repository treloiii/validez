package validez.examples.dto;

import lombok.Data;
import ru.trelloiii.lib.annotation.Validate;
import ru.trelloiii.lib.annotation.validators.*;

import java.util.List;
import java.util.Set;

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
    private Integer age;
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
}
