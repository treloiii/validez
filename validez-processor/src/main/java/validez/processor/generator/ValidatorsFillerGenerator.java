package validez.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import validez.lib.api.Validators;
import validez.processor.utils.ProcessorUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Map;

public class ValidatorsFillerGenerator {

    public TypeSpec generate(Map<TypeElement, JavaFile> validators) {
        CodeBlock.Builder staticInitBlockBuilder = CodeBlock.builder();
        for (Map.Entry<TypeElement, JavaFile> entry : validators.entrySet()) {
            JavaFile validator = entry.getValue();
            TypeElement validClass = entry.getKey();
            ClassName validatorClass = ClassName.get(validator.packageName, validator.typeSpec.name);
            CodeBlock codeBlock = CodeBlock.builder()
                    .addStatement("$T.validators.put($T.class, new $T())", TypeName.get(Validators.class), validClass, validatorClass)
                    .build();
            staticInitBlockBuilder.add(codeBlock);
        }
        return TypeSpec.classBuilder("ValidatorsFiller")
                .addAnnotation(ProcessorUtils.createGenerated(ValidatorsFillerGenerator.class))
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(staticInitBlockBuilder.build())
                .build();
    }

}
