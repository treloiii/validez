package ru.trelloiii.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import ru.trelloiii.lib.annotation.validators.Length;
import ru.trelloiii.processor.config.ConfigProvider;

import javax.lang.model.element.VariableElement;

public class LengthValidator implements FieldValidator<Length> {

    @Override
    public CodeBlock build(Length length, VariableElement field, String delegateName) {
        String fieldName = field.getSimpleName().toString();
        ClassName exceptionType = ConfigProvider.getExceptionClass();
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        int equals = length.equals();
        if (equals != Length.DEFAULT) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() != $L)", fieldName, equals)
                            .addStatement("throw new $T()", exceptionType)
                            .endControlFlow()
                            .build()
            );
        }
        int max = length.max();
        if (max != Length.DEFAULT && equals == Length.DEFAULT) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() > $L)", fieldName, max)
                            .addStatement("throw new $T()", exceptionType)
                            .endControlFlow()
                            .build()
            );
        }
        int min = length.min();
        if (min != Length.DEFAULT && equals == Length.DEFAULT) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() < $L)", fieldName, min)
                            .addStatement("throw new $T()", exceptionType)
                            .endControlFlow()
                            .build()
            );
        }
        return codeBlockBuilder.build();
    }
}
