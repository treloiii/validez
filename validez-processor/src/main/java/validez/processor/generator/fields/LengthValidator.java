package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.Length;
import validez.processor.config.ConfigProvider;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

@RequiredArgsConstructor
public class LengthValidator implements FieldValidator<Length> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(Length length, VariableElement field, String delegateName) {
        String fieldName = field.getSimpleName().toString();
        boolean number = isNumber(field);
        String message = "\"" + length.message() + "\"";
        if (length.format()) {
            message =  CodeBlock.of(message, fieldName).toString();
        }
        ClassName exceptionType = ConfigProvider.getExceptionClass();
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        int equals = length.equals();
        if (equals != Length.DEFAULT && !number) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() != $L)", fieldName, equals)
                            .addStatement("throw new $T($L)", exceptionType, message)
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
                                .addStatement("throw new $T($L)", exceptionType, message)
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N > $L)", fieldName, max)
                                .addStatement("throw new $T($L)", exceptionType, message)
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
                                .addStatement("throw new $T($L)", exceptionType, message)
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N < $L)", fieldName, min)
                                .addStatement("throw new $T($L)", exceptionType, message)
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
