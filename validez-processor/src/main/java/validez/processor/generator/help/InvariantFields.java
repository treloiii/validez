package validez.processor.generator.help;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InvariantFields {

    private final List<String> fields;

}
