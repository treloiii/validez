package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.StringRange;
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
public class StringRangeValidator implements FieldValidator<StringRange> {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(StringRange stringRange, VariableElement field, ValidatorArgs args) {
        consumesValid(field, stringRange, processingEnvironment);
        String[] range = stringRange.value();
        Name fieldName = field.getSimpleName();
        List<String> rangeValues = new ArrayList<>(range.length);
        for (String rangeValue : range) {
            rangeValues.add( "\"" + rangeValue + "\"");
        }
        String rangeLiteral = String.join(",", rangeValues);
        ClassName definedValidator = ClassName.get(InRangeDefinedValidator.class);
        return CodeBlock.builder()
                .beginControlFlow("if (!$T.validateString($N, new String[]{$L}))", definedValidator, fieldName, rangeLiteral)
                .addStatement(returnValidatorContext(fieldName, "value", StringRange.class))
                .endControlFlow()
                .build();
    }

}
