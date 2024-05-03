package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.ByteBound;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

public class ByteBoundValidator extends BoundValidator<ByteBound> implements FieldValidator<ByteBound> {

    public ByteBoundValidator(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public CodeBlock build(ByteBound byteBound, VariableElement field, ValidatorArgs args) {
        return build(byteBound, field, ByteBound::nullValueStrategy);
    }

}
