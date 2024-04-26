package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.NullValueStrategy;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

import static validez.processor.utils.CodeUtils.returnValidatorContext;
import static validez.processor.utils.ProcessorUtils.isFieldSubtypeOf;

@RequiredArgsConstructor
public class LengthValidator extends NullStrategyValidator implements FieldValidator<Length> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(Length length, VariableElement field, ValidatorArgs args) {
        validateFieldType(field);
        Name fieldName = field.getSimpleName();
        boolean number = isNumber(field);
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        NullValueStrategy nullValueStrategy = length.nullStrategy();
        addNullCheckByStrategy(field, nullValueStrategy, codeBlockBuilder, Length.class);
        long equals = length.equals();
        if (equals != Long.MIN_VALUE && !number) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() != $L)", fieldName, equals)
                            .addStatement(returnValidatorContext(fieldName, "equals", Length.class))
                            .endControlFlow()
                            .build()
            );
        }
        long max = length.max();
        if (max != Long.MAX_VALUE && equals == Long.MAX_VALUE) {
            if (!number) {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N.length() > $L)", fieldName, max)
                                .addStatement(returnValidatorContext(fieldName, "max", Length.class))
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N > $L)", fieldName, max)
                                .addStatement(returnValidatorContext(fieldName, "max", Length.class))
                                .endControlFlow()
                                .build()
                );
            }
        }
        long min = length.min();
        if (min != Long.MIN_VALUE && equals == Long.MIN_VALUE) {
            if (!number) {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N.length() < $L)", fieldName, min)
                                .addStatement(returnValidatorContext(fieldName, "min", Length.class))
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N < $L)", fieldName, min)
                                .addStatement(returnValidatorContext(fieldName, "min", Length.class))
                                .endControlFlow()
                                .build()
                );
            }
        }
        return codeBlockBuilder.build();
    }

    private void validateFieldType(VariableElement field) {
        if (isFieldSubtypeOf(field, CharSequence.class, processingEnvironment) ||
                isFieldSubtypeOf(field, Integer.class, processingEnvironment) ||
                isFieldSubtypeOf(field, Long.class, processingEnvironment) ||
                isFieldSubtypeOf(field, Byte.class, processingEnvironment) ||
                isFieldSubtypeOf(field, Character.class, processingEnvironment)) {
            return;
        }
        throw new RuntimeException("@Length can be placed only on CharSequence objects, " +
                "integer-valued types (long, int, byte), characters and it wrappers");
    }

    private boolean isNumber(VariableElement field) {
        return isFieldSubtypeOf(field, Number.class, processingEnvironment);
    }
}
