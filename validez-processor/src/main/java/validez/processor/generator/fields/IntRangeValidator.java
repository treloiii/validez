package validez.processor.generator.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.IntRange;
import validez.lib.api.defined.InRangeDefinedValidator;
import validez.lib.api.messaging.ValidatorContext;
import validez.processor.config.ConfigProvider;
import validez.processor.generator.ValidatorArgs;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

public class IntRangeValidator implements FieldValidator<IntRange> {

    @Override
    public CodeBlock build(IntRange annotation, VariableElement field, ValidatorArgs args) {
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
                .addStatement("$T $NContext = new $T($L, $T.class, $L)",
                        ValidatorContext.class, fieldName, ValidatorContext.class, "\"IntRange\"", IntRange.class, "\"value\"")
                .addStatement("throw new $T($N.handle(\"$L\", $NContext))",
                        ConfigProvider.getExceptionClass(),
                        args.getMessageHandlerName(), fieldName, fieldName)
                .endControlFlow()
                .build();
    }

}
