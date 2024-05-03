package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.IntBound;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

public class IntBoundValidator extends BoundValidator<IntBound> implements FieldValidator<IntBound> {

    public IntBoundValidator(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public CodeBlock build(IntBound intBound, VariableElement field, ValidatorArgs args) {
        return build(intBound, field, IntBound::nullValueStrategy);
    }
}
