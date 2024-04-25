package validez.processor.generator;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import validez.lib.api.defined.FieldUtils;
import validez.processor.generator.fields.FieldValidator;
import validez.processor.generator.fields.external.ExternalAnnotationValidator;
import validez.processor.generator.help.AnnotationAndValidator;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Builder
@RequiredArgsConstructor
public class SimpleField extends ValidField {

    @Getter
    private final VariableElement field;
    private final Map<Annotation, FieldValidator<Annotation>> fieldValidators;
    private final ExternalAnnotationValidator externalAnnotationValidator;
    private final List<AnnotationAndValidator> externalValidators;

    @Override
    public CodeBlock createCode(ValidatorArgs args) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        Name fieldName = field.getSimpleName();
        codeBlockBuilder.addStatement("$T $N = $N.$L", field.asType(), fieldName,
                args.getDelegateName(), generateGetter(field));
        codeBlockBuilder.beginControlFlow("if ($T.needValidate(\"$L\", $N, $N))",
                FieldUtils.class, fieldName.toString(), args.getIncludesName(), args.getExcludesName());
        for (Map.Entry<Annotation, FieldValidator<Annotation>> entry : fieldValidators.entrySet()) {
            Annotation annotation = entry.getKey();
            FieldValidator<Annotation> validator = entry.getValue();
            CodeBlock validatorCode = validator.build(annotation, field, args);
            codeBlockBuilder.add(validatorCode);
        }
        for (AnnotationAndValidator exValidator: externalValidators) {
            CodeBlock validatorCode = externalAnnotationValidator
                    .build(field, exValidator.getAnnotation(), exValidator.getExternalValidatorType(), args);
            codeBlockBuilder.add(validatorCode);
        }
        codeBlockBuilder.endControlFlow();
        codeBlockBuilder.addStatement("return null");
        return codeBlockBuilder.build();
    }

}
