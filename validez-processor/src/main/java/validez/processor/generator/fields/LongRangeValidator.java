package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.LongRange;
import validez.lib.api.defined.InRangeDefinedValidator;
import validez.processor.generator.ValidatorArgs;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static validez.processor.utils.CodeUtils.addContextThrower;

public class LongRangeValidator implements FieldValidator<LongRange> {

    @Override
    public CodeBlock build(LongRange annotation, VariableElement field, ValidatorArgs args) {
        long[] range = annotation.value();
        Name fieldName = field.getSimpleName();
        List<String> rangeValues = new ArrayList<>(range.length);
        for (long rangeValue : range) {
            rangeValues.add(String.valueOf(rangeValue));
        }
        String rangeLiteral = String.join(",", rangeValues);
        ClassName definedValidator = ClassName.get(InRangeDefinedValidator.class);
        return CodeBlock.builder()
                .beginControlFlow("if (!$T.validateLong($N, new long[]{$L}))", definedValidator, fieldName, rangeLiteral)
                .add(addContextThrower(args, fieldName, "value", LongRange.class))
                .endControlFlow()
                .build();
    }

}
