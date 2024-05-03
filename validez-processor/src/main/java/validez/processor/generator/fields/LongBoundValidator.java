package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.LongBound;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

public class LongBoundValidator extends BoundValidator<LongBound> implements FieldValidator<LongBound> {

    public LongBoundValidator(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public CodeBlock build(LongBound longBound, VariableElement field, ValidatorArgs args) {
        return build(longBound, field, LongBound::nullValueStrategy);
    }
}
