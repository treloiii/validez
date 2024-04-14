package ru.trelloiii.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import ru.trelloiii.lib.annotation.validators.NotEmpty;
import ru.trelloiii.processor.config.ConfigProvider;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collection;

@RequiredArgsConstructor
public class NotEmptyValidator implements FieldValidator<NotEmpty> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(NotEmpty annotation, VariableElement field, String delegateName) {
        Name fieldName = field.getSimpleName();
        CodeBlock.Builder notEmptyBuilder = CodeBlock.builder();
        CodeBlock nullCheck = CodeBlock.builder()
                .beginControlFlow("if ($N == null)", fieldName)
                .addStatement("throw new $T()", ConfigProvider.getExceptionClass())
                .endControlFlow()
                .build();
        notEmptyBuilder.add(nullCheck);
        boolean stringOrCollection = isStringOrCollection(field);
        if (stringOrCollection) {
            CodeBlock emptyCheck = CodeBlock.builder()
                    .beginControlFlow("if ($N.isEmpty())", fieldName)
                    .addStatement("throw new $T()", ConfigProvider.getExceptionClass())
                    .endControlFlow()
                    .build();
            notEmptyBuilder.add(emptyCheck);
        }
        return notEmptyBuilder.build();
    }

    private boolean isStringOrCollection(VariableElement field) {
        Types typeUtils = processingEnvironment.getTypeUtils();
        TypeMirror fieldType = typeUtils.erasure(field.asType());
        Elements elementUtils = processingEnvironment.getElementUtils();
        TypeElement stringElement = elementUtils.getTypeElement(String.class.getCanonicalName());
        TypeMirror stringType = stringElement.asType();
        TypeElement collectionElement = elementUtils.getTypeElement(Collection.class.getCanonicalName());
        TypeMirror collectionType = typeUtils.erasure(collectionElement.asType());
        boolean isString = typeUtils.isSubtype(fieldType, stringType);
        boolean isCollection = typeUtils.isSubtype(fieldType, collectionType);
        return isString || isCollection;
    }
}
