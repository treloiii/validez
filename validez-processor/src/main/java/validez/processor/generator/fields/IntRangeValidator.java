package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.IntRange;
import validez.lib.api.defined.InRangeDefinedValidator;
import validez.processor.config.ConfigProvider;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

public class IntRangeValidator implements FieldValidator<IntRange> {

    @Override
    public CodeBlock build(IntRange annotation, VariableElement field, String delegateName) {
        int[] range = annotation.value();
        Name fieldName = field.getSimpleName();
        String message = "\"" + annotation.message() + "\"";
        if (annotation.format()) {
            message = CodeBlock.of(message, fieldName).toString();
        }
        List<String> rangeValues = new ArrayList<>(range.length);
        for (int rangeValue : range) {
            rangeValues.add(String.valueOf(rangeValue));
        }
        String rangeLiteral = String.join(",", rangeValues);
        ClassName definedValidator = ClassName.get(InRangeDefinedValidator.class);
        return CodeBlock.builder()
                .beginControlFlow("if (!$T.validateInt($N, new int[]{$L}))", definedValidator, fieldName, rangeLiteral)
                .addStatement("throw new $T($L)", ConfigProvider.getExceptionClass(), message)
                .endControlFlow()
                .build();
    }

}
