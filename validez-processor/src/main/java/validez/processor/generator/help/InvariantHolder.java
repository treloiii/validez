package validez.processor.generator.help;

import lombok.Data;

import java.util.List;

@Data
public class InvariantHolder {

    private String name;
    private List<InvariantFields> invariantFields;

}
