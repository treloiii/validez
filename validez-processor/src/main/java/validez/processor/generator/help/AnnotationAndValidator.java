package validez.processor.generator.help;

import lombok.AllArgsConstructor;
import lombok.Data;
import validez.processor.utils.StringUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

@Data
@AllArgsConstructor
public class AnnotationAndValidator {

    private final AnnotationMirror annotation;
    private final TypeMirror externalValidatorType;

    public String memberName() {
        String typeString = externalValidatorType.toString();
        String[] split = typeString.split("\\.");
        String name = split[split.length - 1];
        return "$$" + StringUtils.deCapitalize(name);
    }

}
