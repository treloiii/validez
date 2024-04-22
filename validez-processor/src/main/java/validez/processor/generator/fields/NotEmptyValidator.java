package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.NotEmpty;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.Collection;

import static validez.processor.utils.CodeUtils.addContextThrower;
import static validez.processor.utils.ProcessorUtils.isFieldSubtypeOf;

@RequiredArgsConstructor
public class NotEmptyValidator implements FieldValidator<NotEmpty> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(NotEmpty annotation, VariableElement field, ValidatorArgs args) {
        Name fieldName = field.getSimpleName();
        CodeBlock.Builder notEmptyBuilder = CodeBlock.builder();
        CodeBlock nullCheck = CodeBlock.builder()
                .beginControlFlow("if ($N == null)", fieldName)
                .add(addContextThrower(args, fieldName, null, NotEmpty.class))
                .endControlFlow()
                .build();
        notEmptyBuilder.add(nullCheck);
        boolean stringOrCollection = isStringOrCollection(field);
        if (stringOrCollection) {
            CodeBlock emptyCheck = CodeBlock.builder()
                    .beginControlFlow("if ($N.isEmpty())", fieldName)
                    .add(addContextThrower(args, fieldName, null, NotEmpty.class))
                    .endControlFlow()
                    .build();
            notEmptyBuilder.add(emptyCheck);
        }
        return notEmptyBuilder.build();
    }

    private boolean isStringOrCollection(VariableElement field) {
        boolean isString = isFieldSubtypeOf(field, String.class, processingEnvironment);
        boolean isCollection = isFieldSubtypeOf(field, Collection.class, processingEnvironment);
        return isString || isCollection;
    }
}
