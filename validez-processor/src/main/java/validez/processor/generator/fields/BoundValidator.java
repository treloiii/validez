package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import lombok.RequiredArgsConstructor;
import validez.lib.annotation.validators.NullValueStrategy;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Function;

import static validez.processor.generator.fields.help.ValidatorsUtils.consumesValid;
import static validez.processor.utils.CodeUtils.returnValidatorContext;
import static validez.processor.utils.ProcessorUtils.getAnnotationValues;
import static validez.processor.utils.ProcessorUtils.getAnnotationsOfType;
import static validez.processor.utils.ProcessorUtils.isFieldSubtypeOf;

@RequiredArgsConstructor
public abstract class BoundValidator<T extends Annotation> extends NullStrategyValidator {

    private final ProcessingEnvironment processingEnvironment;

    public CodeBlock build(T bound, VariableElement field, Function<T, NullValueStrategy> nullValueStrategyExtractor) {
        consumesValid(field, bound, processingEnvironment);
        boolean isLong = isFieldSubtypeOf(field, Long.class, processingEnvironment);
        String longLiteral = isLong ? "L" : "";
        Name fieldName = field.getSimpleName();
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        NullValueStrategy nullValueStrategy = nullValueStrategyExtractor.apply(bound);
        addNullCheckByStrategy(field, nullValueStrategy, codeBlockBuilder,
                bound.annotationType(), "nullValueStrategy");
        Elements elements = processingEnvironment.getElementUtils();
        AnnotationMirror boundMirror = getAnnotationsOfType(bound.annotationType(), field, elements);
        //impossible to be null
        assert boundMirror != null;
        Map<String, AnnotationValue> boundValues
                = getAnnotationValues(boundMirror);
        AnnotationValue equals = boundValues.get("equals");
        if (equals != null) {
            Object equalsValue = equals.getValue();
            codeBlockBuilder.add(
                    CodeBlock.builder()
                            .beginControlFlow("if ($N != $L$L)", fieldName, equalsValue, longLiteral)
                            .addStatement(returnValidatorContext(fieldName, "equals", bound.annotationType()))
                            .endControlFlow()
                            .build()
            );
        } else {
            AnnotationValue min = boundValues.get("min");
            if (min != null) {
                Object minValue = min.getValue();
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N < $L$L)", fieldName, minValue, longLiteral)
                                .addStatement(returnValidatorContext(fieldName, "min", bound.annotationType()))
                                .endControlFlow()
                                .build()
                );
            }
            AnnotationValue max = boundValues.get("max");
            if (max != null) {
                Object maxValue = max.getValue();
                codeBlockBuilder.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($N > $L$L)", fieldName, maxValue, longLiteral)
                                .addStatement(returnValidatorContext(fieldName, "max", bound.annotationType()))
                                .endControlFlow()
                                .build()
                );
            }
        }
        return codeBlockBuilder.build();
    }

}
