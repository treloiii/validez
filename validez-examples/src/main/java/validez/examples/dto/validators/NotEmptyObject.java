package validez.examples.dto.validators;

import lombok.Data;
import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.NotEmpty;

import java.util.List;
import java.util.Set;

@Validate
@Data
public class NotEmptyObject {

    @NotEmpty
    private List<Object> list;
    @NotEmpty
    private Set<Object> set;
    @NotEmpty
    private String string;
    @NotEmpty
    private Object obj;

}
