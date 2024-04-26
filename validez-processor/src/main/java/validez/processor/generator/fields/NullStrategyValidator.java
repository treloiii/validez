package validez.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;
import validez.lib.annotation.validators.NullValueStrategy;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;

import static validez.processor.utils.CodeUtils.returnValidatorContext;
import static validez.processor.utils.ProcessorUtils.isFieldPrimitive;

public abstract class NullStrategyValidator {

    public void addNullCheckByStrategy(VariableElement field,
                                       NullValueStrategy strategy,
                                       CodeBlock.Builder fieldCodeBuilder,
                                       Class<? extends Annotation> annotation,
                                       String propertyName) {
        if (!isFieldPrimitive(field)) {
            Name fieldName = field.getSimpleName();
            fieldCodeBuilder
                    .beginControlFlow("if ($N == null)", fieldName);
            if (NullValueStrategy.NULL_ALLOWED.equals(strategy)) {
                fieldCodeBuilder
                        .addStatement("return null");
            } else {
                fieldCodeBuilder
                        .addStatement(returnValidatorContext(fieldName, propertyName, annotation));
            }
            fieldCodeBuilder.endControlFlow();
        }
    }

}
