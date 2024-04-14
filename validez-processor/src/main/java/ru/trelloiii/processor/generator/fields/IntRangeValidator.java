package ru.trelloiii.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import ru.trelloiii.lib.annotation.validators.IntRange;
import ru.trelloiii.lib.api.defined.InRangeDefinedValidator;
import ru.trelloiii.processor.config.ConfigProvider;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

public class IntRangeValidator implements FieldValidator<IntRange> {

    @Override
    public CodeBlock build(IntRange annotation, VariableElement field, String delegateName) {
        int[] range = annotation.value();
        Name fieldName = field.getSimpleName();
        List<String> rangeValues = new ArrayList<>(range.length);
        for (int rangeValue : range) {
            rangeValues.add(String.valueOf(rangeValue));
        }
        String rangeLiteral = String.join(",", rangeValues);
        ClassName definedValidator = ClassName.get(InRangeDefinedValidator.class);
        return CodeBlock.builder()
                .beginControlFlow("if (!$T.validateInt($N, new int[]{$L}))", definedValidator, fieldName, rangeLiteral)
                .addStatement("throw new $T()", ConfigProvider.getExceptionClass())
                .endControlFlow()
                .build();
    }

}
