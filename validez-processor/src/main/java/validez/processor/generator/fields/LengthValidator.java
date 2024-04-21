package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.api.messaging.ValidatorContext;
import validez.processor.config.ConfigProvider;
import validez.processor.generator.ValidatorArgs;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

@RequiredArgsConstructor
public class LengthValidator implements FieldValidator<Length> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(Length length, VariableElement field, ValidatorArgs args) {
        String fieldName = field.getSimpleName().toString();
        boolean number = isNumber(field);
        ClassName exceptionType = ConfigProvider.getExceptionClass();
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        int equals = length.equals();
        if (equals != Length.DEFAULT && !number) {
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N.length() != $L)", fieldName, equals)
                            .addStatement(getValidatorContext(fieldName, "equals"))
                            .addStatement("throw new $T($L)", exceptionType, getValidatorContextArg(args, fieldName))
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
                                .addStatement(getValidatorContext(fieldName, "max"))
                                .addStatement("throw new $T($L)", exceptionType, getValidatorContextArg(args, fieldName))
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N > $L)", fieldName, max)
                                .addStatement(getValidatorContext(fieldName, "max"))
                                .addStatement("throw new $T($L)", exceptionType, getValidatorContextArg(args, fieldName))
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
                                .addStatement(getValidatorContext(fieldName, "min"))
                                .addStatement("throw new $T($L)", exceptionType, getValidatorContextArg(args, fieldName))
                                .endControlFlow()
                                .build()
                );
            } else {
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N < $L)", fieldName, min)
                                .addStatement(getValidatorContext(fieldName, "min"))
                                .addStatement("throw new $T($L)", exceptionType, getValidatorContextArg(args, fieldName))
                                .endControlFlow()
                                .build()
                );
            }
        }
        return codeBlockBuilder.build();
    }

    private String getValidatorContextArg(ValidatorArgs args, String fieldName) {
        return CodeBlock.of("$N.handle(\"$L\", $NContext)", args.getMessageHandlerName(),
                        fieldName, fieldName)
                .toString();
    }

    private CodeBlock getValidatorContext(String fieldName, String property) {
        return CodeBlock.of("$T $NContext = new $T($L, $T.class, $L)",
                ValidatorContext.class, fieldName, ValidatorContext.class,
                "\"Length\"", Length.class, "\"" + property + "\"");
    }

    private boolean isNumber(VariableElement field) {
        return ProcessorUtils.isFieldSubtypeOf(field, Number.class, processingEnvironment);
    }
}
