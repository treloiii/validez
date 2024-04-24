package validez.processor.utils;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.api.messaging.ValidatorContext;
import validez.processor.config.ConfigProvider;
import validez.processor.generator.ValidatorArgs;

import javax.lang.model.element.Name;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeUtils {

    public static CodeBlock throwWithContext(Name fieldName, String contextName, ValidatorArgs args) {
        Map<String, Object> named = new LinkedHashMap<>();
        named.put("field", fieldName);
        named.put("exception", ConfigProvider.getExceptionClass());
        named.put("handler", args.getMessageHandlerName());
        named.put("context", contextName);
        return CodeBlock.builder()
                .addStatement(
                        CodeBlock.builder()
                                .addNamed("throw new $exception:T($handler:N.handle($field:S, $context:L))", named)
                                .build()
                )
                .build();
    }

    public static CodeBlock throwWithContextInvariant(String invariantName, ValidatorArgs args,
                                                      Map<String, String> fieldToContext) {
        Map<String, Object> named = new LinkedHashMap<>();
        named.put("exception", ConfigProvider.getExceptionClass());
        named.put("handler", args.getMessageHandlerName());
        named.put("invariant", invariantName);
        String membersContextVar = "$$membersContext";
        named.put("membersContext", membersContextVar);
        CodeBlock.Builder membersPutCodeBuilder = CodeBlock.builder();
        for (Map.Entry<String, String> entry : fieldToContext.entrySet()) {
            String fieldName = entry.getKey();
            String contextVarName = entry.getValue();
            membersPutCodeBuilder.addStatement("if ($N != null) $N.put($S, $N)",
                    contextVarName,
                    membersContextVar,
                    fieldName,
                    contextVarName);
        }
        ParameterizedTypeName mapType = ParameterizedTypeName.get(Map.class, String.class, ValidatorContext.class);
        TypeName linkedHashMap = ParameterizedTypeName.get(LinkedHashMap.class);
        return CodeBlock.builder()
                .addStatement("$T $N = new $T()", mapType, membersContextVar, linkedHashMap)
                .add(membersPutCodeBuilder.build())
                .addStatement(
                        CodeBlock.builder()
                                .addNamed("throw new $exception:T($handler:N.handleInvariant($invariant:S, $membersContext:L))", named)
                                .build()
                )
                .build();
    }

    public static CodeBlock returnValidatorContext(Name fieldName,
                                             String propertyName,
                                             Class<? extends Annotation> annotation) {
        Map<String, Object> named = new LinkedHashMap<>();
        named.put("contextClass", ValidatorContext.class);
        named.put("field", fieldName);
        named.put("annotationClass", annotation);
        named.put("annotationName", annotation.getSimpleName());
        named.put("property", propertyName);
        return CodeBlock.builder()
                .addNamed("return new $contextClass:T($annotationName:S, $annotationClass:T.class, $property:S, $field:L)", named)
                .build();
    }

}
