package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.Length;
import validez.processor.generator.ValidatorArgs;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

import static validez.processor.utils.CodeUtils.addContextThrower;

@RequiredArgsConstructor
public class LengthValidator implements FieldValidator<Length> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(Length length, VariableElement field, ValidatorArgs args) {
        Name fieldName = field.getSimpleName();
        boolean number = isNumber(field);
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        int equals = length.equals();
        if (equals != Length.DEFAULT && !number) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() != $L)", fieldName, equals)
                            .add(addContextThrower(args, fieldName, "equals", Length.class))
                            .endControlFlow()
                            .build()
            );
        }
        int max = length.max();
        if (max != Length.DEFAULT && equals == Length.DEFAULT) {
            if (!number) {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N.length() > $L)", fieldName, max)
                                .add(addContextThrower(args, fieldName, "max", Length.class))
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N > $L)", fieldName, max)
                                .add(addContextThrower(args, fieldName, "max", Length.class))
                                .endControlFlow()
                                .build()
                );
            }
        }
        int min = length.min();
        if (min != Length.DEFAULT && equals == Length.DEFAULT) {
            if (!number) {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N.length() < $L)", fieldName, min)
                                .add(addContextThrower(args, fieldName, "min", Length.class))
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N < $L)", fieldName, min)
                                .add(addContextThrower(args, fieldName, "min", Length.class))
                                .endControlFlow()
                                .build()
                );
            }
        }
        return codeBlockBuilder.build();
    }

    private boolean isNumber(VariableElement field) {
        return ProcessorUtils.isFieldSubtypeOf(field, Number.class, processingEnvironment);
    }
}
