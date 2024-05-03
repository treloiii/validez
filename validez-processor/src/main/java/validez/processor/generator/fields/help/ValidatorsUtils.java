package validez.processor.generator.fields.help;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.annotation.internal.Consumes;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static validez.processor.utils.ProcessorUtils.isFieldSubtypeOf;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorsUtils {

    public static void consumesValid(VariableElement field, Annotation annotation,
                                 ProcessingEnvironment processingEnvironment) {
        Class<? extends Annotation> annotationClass = annotation.annotationType();
        Consumes consumes = annotationClass
                .getDeclaredAnnotation(Consumes.class);
        if (consumes == null) {
            return;
        }
        Class<?>[] classes = consumes.value();
        for (Class<?> clazz : classes) {
            if (isFieldSubtypeOf(field, clazz, processingEnvironment)) {
                return;
            }
        }
        String typeNames = Stream.of(classes)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
        String annotationName = annotationClass.getSimpleName();
        throw new RuntimeException("@%s can be placed only on %s value types or it subtypes".formatted(
                annotationName, typeNames
        ));
    }

}
