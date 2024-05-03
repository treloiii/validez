package validez.processor.utils;

import com.squareup.javapoet.AnnotationSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.annotation.ValidatorThrows;
import validez.processor.config.ConfigProvider;

import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProcessorUtils {

    public static AnnotationSpec createGenerated(Class<?> generator) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", generator.getCanonicalName())
                .addMember("date", "$S", LocalDateTime.now().toString())
                .addMember("comments", "$S", "Validez version %s".formatted(ConfigProvider.getProcessorVersion()))
                .build();
    }

    public static List<ExecutableElement> getMethods(TypeElement classElement) {
        List<? extends Element> enclosedElements =
                classElement.getEnclosedElements();
        List<ExecutableElement> methods = new ArrayList<>();
        for (Element element : enclosedElements) {
            if (element instanceof ExecutableElement) {
                methods.add((ExecutableElement) element);
            }
        }
        return methods;
    }

    public static List<VariableElement> getFields(TypeElement classElement) {
        List<? extends Element> enclosedElements =
                classElement.getEnclosedElements();
        List<VariableElement> fields = new ArrayList<>();
        for (Element element : enclosedElements) {
            if (element instanceof VariableElement) {
                fields.add((VariableElement) element);
            }
        }
        return fields;
    }

    public static boolean isFieldSubtypeOf(VariableElement field, Class<?> superClass,
                                           ProcessingEnvironment processingEnvironment) {
        Types typeUtils = processingEnvironment.getTypeUtils();
        TypeMirror fieldType = typeUtils.erasure(field.asType());
        TypeKind fieldKind = fieldType.getKind();
        if (fieldKind.isPrimitive()) {
            fieldType = typeUtils.boxedClass((PrimitiveType) fieldType).asType();
        }
        Elements elementUtils = processingEnvironment.getElementUtils();
        TypeElement superElement = elementUtils.getTypeElement(superClass.getCanonicalName());
        TypeMirror superType = typeUtils.erasure(superElement.asType());
        return typeUtils.isSubtype(fieldType, superType);
    }

    @Nullable
    public static Object getAnnotationValue(String name, Class<? extends Annotation> annotation,
                                            Element element, Elements elements) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String annotationType = annotationMirror.getAnnotationType().toString();
            if (annotationType.equals(annotation.getCanonicalName())) {
                Map<? extends ExecutableElement, ? extends AnnotationValue> values = elements.getElementValuesWithDefaults(annotationMirror);
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
                    ExecutableElement propElement = entry.getKey();
                    String propName = propElement.getSimpleName().toString();
                    if (propName.equals(name)) {
                        AnnotationValue annotationValue = entry.getValue();
                        return annotationValue.getValue();
                    }
                }
            }
        }
        return null;
    }

    public static List<Object> getAnnotationsValues(String name, Class<? extends Annotation> annotation,
                                                   Element element, Elements elements) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        return getAnnotationsValuesFromMirrors(name, annotation, annotationMirrors, elements);
    }

    public static List<Object> getAnnotationsValuesFromMirrors(String name, Class<? extends Annotation> annotation,
                                                               List<? extends AnnotationMirror> annotationMirrors, Elements elements) {
        List<Object> result = new ArrayList<>();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String annotationType = annotationMirror.getAnnotationType().toString();
            if (annotationType.equals(annotation.getCanonicalName())) {
                Map<? extends ExecutableElement, ? extends AnnotationValue> values = elements.getElementValuesWithDefaults(annotationMirror);
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
                    ExecutableElement propElement = entry.getKey();
                    String propName = propElement.getSimpleName().toString();
                    if (propName.equals(name)) {
                        AnnotationValue annotationValue = entry.getValue();
                        result.add(annotationValue.getValue());
                    }
                }
            }
        }
        return result;
    }

    @Nullable
    public static AnnotationMirror getAnnotationsOfType(Class<? extends Annotation> annotationClass,
                                                        Element element, Elements elements) {
        List<? extends AnnotationMirror> annotationMirrors = elements.getAllAnnotationMirrors(element);
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String annotationCanonicalClass = annotationMirror.getAnnotationType().toString();
            if (annotationCanonicalClass.equals(annotationClass.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    public static Map<String, AnnotationValue> getAnnotationValues(AnnotationMirror mirror) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> values = mirror.getElementValues();
        Map<String, AnnotationValue> resultValues = new HashMap<>();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
            ExecutableElement executableElement = entry.getKey();
            AnnotationValue annotationValue = entry.getValue();
            String name = executableElement.getSimpleName().toString();
            resultValues.put(name, annotationValue);
        }
        return resultValues;
    }

    public static boolean isFieldPrimitive(VariableElement field) {
        TypeKind typeKind = field.asType().getKind();
        return typeKind.isPrimitive();
    }

    @Nullable
    public static String parseException(TypeElement validateClassElement, Elements elements) {
        Object exceptionClass = ProcessorUtils.getAnnotationValue("value", ValidatorThrows.class,
                validateClassElement, elements);
        return Optional.ofNullable(exceptionClass)
                .map(String::valueOf)
                .orElse(null);
    }
}
