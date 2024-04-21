package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.processor.generator.ValidatorArgs;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;

public interface FieldValidator<T extends Annotation> {

    CodeBlock build(T annotation, VariableElement field, ValidatorArgs args);

}
