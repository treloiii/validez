package validez.processor.generator;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import validez.lib.api.defined.FieldUtils;
import validez.processor.generator.fields.FieldValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Map;

@Builder
@RequiredArgsConstructor
public class SimpleField extends ValidField {

    private final VariableElement field;
    private final Map<Annotation, FieldValidator<Annotation>> fieldValidators;

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
            CodeBlock validatorCode = validator.build(annotation, field, args.getDelegateName());
            codeBlockBuilder.add(validatorCode);
        }
        codeBlockBuilder.endControlFlow();
        return codeBlockBuilder.build();
    }

}
