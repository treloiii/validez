package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.ShortBound;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

public class ShortBoundValidator extends BoundValidator<ShortBound> implements FieldValidator<ShortBound> {

    public ShortBoundValidator(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public CodeBlock build(ShortBound shortBound, VariableElement field, ValidatorArgs args) {
        return build(shortBound, field, ShortBound::nullValueStrategy);
    }

}
