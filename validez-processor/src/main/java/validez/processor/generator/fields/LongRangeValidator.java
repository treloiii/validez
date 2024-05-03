package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.LongRange;
import validez.lib.api.defined.InRangeDefinedValidator;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static validez.processor.generator.fields.help.ValidatorsUtils.consumesValid;
import static validez.processor.utils.CodeUtils.returnValidatorContext;

@RequiredArgsConstructor
public class LongRangeValidator implements FieldValidator<LongRange> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(LongRange longRange, VariableElement field, ValidatorArgs args) {
        consumesValid(field, longRange, processingEnvironment);
        long[] range = longRange.value();
        Name fieldName = field.getSimpleName();
        List<String> rangeValues = new ArrayList<>(range.length);
        for (long rangeValue : range) {
            rangeValues.add(String.valueOf(rangeValue) + "L");
        }
        String rangeLiteral = String.join(",", rangeValues);
        ClassName definedValidator = ClassName.get(InRangeDefinedValidator.class);
        return CodeBlock.builder()
                .beginControlFlow("if (!$T.validateLong($N, new long[]{$L}))", definedValidator, fieldName, rangeLiteral)
                .addStatement(returnValidatorContext(fieldName, "value", LongRange.class))
                .endControlFlow()
                .build();
    }

}
