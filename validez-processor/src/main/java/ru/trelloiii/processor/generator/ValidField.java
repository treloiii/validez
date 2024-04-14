package ru.trelloiii.processor.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.trelloiii.lib.annotation.validators.Length;
import ru.trelloiii.lib.annotation.validators.NotEmpty;
import ru.trelloiii.lib.exceptions.InvalidException;
import ru.trelloiii.processor.generator.fields.FieldValidator;

import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Builder
@RequiredArgsConstructor
public class ValidField {

    private static final String GETTER_PATTERN = "get%s()";

    private final ProcessingEnvironment processingEnvironment;
    private final VariableElement field;
    private final Map<Annotation, FieldValidator<Annotation>> fieldValidators;

    public CodeBlock createCode(String delegateName) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        Name fieldName = field.getSimpleName();
        codeBlockBuilder.addStatement("$T $N = $N.$L", field.asType(), fieldName, delegateName, generateGetter());
        for (Map.Entry<Annotation, FieldValidator<Annotation>> entry : fieldValidators.entrySet()) {
            Annotation annotation = entry.getKey();
            FieldValidator<Annotation> validator = entry.getValue();
            CodeBlock validatorCode = validator.build(annotation, field, delegateName);
            codeBlockBuilder.add(validatorCode);
        }
        return codeBlockBuilder.build();
    }

    private String generateGetter() {
        String fieldName = field.getSimpleName().toString();
        fieldName = capitalize(fieldName);
        return GETTER_PATTERN.formatted(fieldName);
    }

    private static String capitalize(String val) {
        byte[] bytes = val.getBytes();
        bytes[0] = (byte) Character.toUpperCase((char) bytes[0]);
        return new String(bytes);
    }
}
