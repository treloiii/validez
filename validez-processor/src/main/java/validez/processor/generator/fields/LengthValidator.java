package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.NullValueStrategy;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

import static validez.processor.generator.fields.help.ValidatorsUtils.consumesValid;
import static validez.processor.utils.CodeUtils.returnValidatorContext;

@RequiredArgsConstructor
public class LengthValidator extends NullStrategyValidator implements FieldValidator<Length> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(Length length, VariableElement field, ValidatorArgs args) {
        consumesValid(field, length, processingEnvironment);
        Name fieldName = field.getSimpleName();
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        NullValueStrategy nullValueStrategy = length.nullValueStrategy();
        addNullCheckByStrategy(field, nullValueStrategy, codeBlockBuilder,
                Length.class, "nullStrategy");
        long equals = length.equals();
        if (equals != Integer.MIN_VALUE) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() != $L)", fieldName, equals)
                            .addStatement(returnValidatorContext(fieldName, "equals", Length.class))
                            .endControlFlow()
                            .build()
            );
        }
        long max = length.max();
        if (max != Integer.MAX_VALUE && equals == Integer.MIN_VALUE) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() > $L)", fieldName, max)
                            .addStatement(returnValidatorContext(fieldName, "max", Length.class))
                            .endControlFlow()
                            .build()
            );
        }
        long min = length.min();
        if (min != Integer.MIN_VALUE && equals == Integer.MIN_VALUE) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() < $L)", fieldName, min)
                            .addStatement(returnValidatorContext(fieldName, "min", Length.class))
                            .endControlFlow()
                            .build()
            );
        }
        return codeBlockBuilder.build();
    }
}
