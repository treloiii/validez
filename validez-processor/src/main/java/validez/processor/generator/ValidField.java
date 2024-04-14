package validez.processor.generator;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import validez.processor.generator.fields.FieldValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
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
