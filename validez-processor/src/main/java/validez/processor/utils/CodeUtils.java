package validez.processor.utils;

import com.squareup.javapoet.CodeBlock;
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

    public static CodeBlock addContextThrower(ValidatorArgs args,
                                              Name fieldName,
                                              String propertyName,
                                              Class<? extends Annotation> annotation) {
        Map<String, Object> named = new LinkedHashMap<>();
        named.put("contextClass", ValidatorContext.class);
        named.put("field", fieldName);
        named.put("annotationClass", annotation);
        named.put("annotationName", annotation.getSimpleName());
        named.put("property", propertyName);
        named.put("exception", ConfigProvider.getExceptionClass());
        named.put("handler", args.getMessageHandlerName());
        CodeBlock newContext = CodeBlock.builder()
                .addNamed("new $contextClass:T($annotationName:S, $annotationClass:T.class, $property:S, $field:L)", named)
                .build();
        named.put("context", newContext);
        return CodeBlock.builder()
                .addStatement(
                        CodeBlock.builder()
                                .addNamed("throw new $exception:T($handler:N.handle($field:S, $context:L))", named)
                                .build()
                )
                .build();
    }

}
