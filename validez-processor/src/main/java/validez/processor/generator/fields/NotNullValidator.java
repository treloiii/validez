package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.NotNull;
import validez.lib.annotation.validators.NullValueStrategy;
import validez.processor.generator.ValidatorArgs;

import javax.lang.model.element.VariableElement;

public class NotNullValidator extends NullStrategyValidator implements FieldValidator<NotNull> {
    @Override
    public CodeBlock build(NotNull annotation, VariableElement field, ValidatorArgs args) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        addNullCheckByStrategy(field, NullValueStrategy.NULL_NOT_ALLOWED,
                codeBlockBuilder, NotNull.class, null);
        return codeBlockBuilder.build();
    }
}
