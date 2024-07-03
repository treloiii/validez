package validez.processor.generator;

import com.squareup.javapoet.CodeBlock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.conditions.Partial;
import validez.lib.api.Validators;
import validez.lib.api.data.ValidationResult;
import validez.lib.api.defined.FieldUtils;

import javax.annotation.Nullable;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static validez.processor.utils.CodeUtils.returnValidatorContextForComplex;

@Getter
@RequiredArgsConstructor
public class ComplexField extends ValidField {

    private final Types types;
    private final Elements elements;
    private final VariableElement field;

    @Override
    public CodeBlock createCode(ValidatorArgs args) {
        Name fieldName = field.getSimpleName();
        TypeMirror fieldType = field.asType();
        String[] includes = null;
        String[] excludes = null;
        Partial partial = field.getAnnotation(Partial.class);
        if (partial != null) {
            String[] exclude = partial.exclude();
            String[] include = partial.include();
            if (exclude.length != 0 && include.length != 0) {
                throw new IllegalArgumentException("@Partial cannot use non-empty exclude and include at the same time");
            }
            if (exclude.length > 0) {
                excludes = exclude;
            } else {
                includes = include;
            }
        }
        String internalResultName = "$$internalResult";
        return CodeBlock.builder()
                .addStatement("$T $N = $N.$L", fieldType, fieldName,
                        args.getDelegateName(), generateGetter(field))
                .beginControlFlow("if ($T.needValidate(\"$L\", $N, $N))",
                        FieldUtils.class, fieldName.toString(), args.getIncludesName(), args.getExcludesName())
                .addStatement(
                        CodeBlock.of("$T $L = $T.forClass($T.class)\n.validate($N, $L, $L)",
                                ValidationResult.class, internalResultName,
                                Validators.class, fieldType, fieldName,
                                parsePartialValue(includes),
                                parsePartialValue(excludes)
                        )
                )
                .beginControlFlow("if (!$L.isValid())", internalResultName)
                .addStatement(returnValidatorContextForComplex(fieldName, null, internalResultName))
                .endControlFlow()
                .endControlFlow()
                .addStatement("return null")
                .build();
    }

    private String parsePartialValue(@Nullable String[] arr) {
        if (arr == null || arr.length == 0) {
            return "null";
        }
        StringBuilder builder = new StringBuilder("java.util.Set.of(");
        int index = 0;
        for (String val : arr) {
            builder.append("\"").append(val).append("\"");
            if (index < arr.length - 1) {
                builder.append(",");
            }
            index++;
        }
        builder.append(")");
        return builder.toString();
    }

}
