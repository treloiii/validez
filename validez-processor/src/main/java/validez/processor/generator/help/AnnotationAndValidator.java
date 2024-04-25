package validez.processor.generator.help;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

@Data
@AllArgsConstructor
public class AnnotationAndValidator {

    private final AnnotationMirror annotation;
    private final TypeMirror externalValidatorType;

}
