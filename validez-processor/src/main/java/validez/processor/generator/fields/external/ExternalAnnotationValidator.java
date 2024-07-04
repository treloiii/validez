package validez.processor.generator.fields.external;

import com.squareup.javapoet.CodeBlock;
import validez.processor.generator.help.AnnotationAndValidator;

import javax.lang.model.element.VariableElement;

public interface ExternalAnnotationValidator {

    CodeBlock build(VariableElement field, AnnotationAndValidator validatorMeta);

}
