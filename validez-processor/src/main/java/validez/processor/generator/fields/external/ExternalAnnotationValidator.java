package validez.processor.generator.fields.external;

import com.squareup.javapoet.CodeBlock;
import validez.processor.generator.ValidatorArgs;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public interface ExternalAnnotationValidator {

    CodeBlock build(VariableElement field, AnnotationMirror annotation,
                    TypeMirror externalValidatorType, ValidatorArgs args);

}
