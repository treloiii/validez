package validez.processor.generator;

import com.squareup.javapoet.*;
import validez.lib.api.Validators;

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
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(staticInitBlockBuilder.build())
                .build();
    }

}
