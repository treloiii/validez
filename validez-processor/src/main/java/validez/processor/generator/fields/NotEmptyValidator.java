package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.NotEmpty;
import validez.processor.config.ConfigProvider;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.Collection;

@RequiredArgsConstructor
public class NotEmptyValidator implements FieldValidator<NotEmpty> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(NotEmpty annotation, VariableElement field, String delegateName) {
        Name fieldName = field.getSimpleName();
        CodeBlock.Builder notEmptyBuilder = CodeBlock.builder();
        String message =  "\"" + annotation.message() + "\"";
        if (annotation.format()) {
            message = CodeBlock.of(message, fieldName).toString();
        }
        ClassName exceptionClass = ConfigProvider.getExceptionClass();
        CodeBlock nullCheck = CodeBlock.builder()
                .beginControlFlow("if ($N == null)", fieldName)
                .addStatement("throw new $T($L)", exceptionClass, message)
                .endControlFlow()
                .build();
        notEmptyBuilder.add(nullCheck);
        boolean stringOrCollection = isStringOrCollection(field);
        if (stringOrCollection) {
            CodeBlock emptyCheck = CodeBlock.builder()
                    .beginControlFlow("if ($N.isEmpty())", fieldName)
                    .addStatement("throw new $T($L)", exceptionClass, message)
                    .endControlFlow()
                    .build();
            notEmptyBuilder.add(emptyCheck);
        }
        return notEmptyBuilder.build();
    }

    private boolean isStringOrCollection(VariableElement field) {
        boolean isString = ProcessorUtils.isFieldSubtypeOf(field, String.class, processingEnvironment);
        boolean isCollection = ProcessorUtils.isFieldSubtypeOf(field, Collection.class, processingEnvironment);
        return isString || isCollection;
    }
}
