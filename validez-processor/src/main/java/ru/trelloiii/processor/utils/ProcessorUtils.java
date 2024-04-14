package ru.trelloiii.processor.utils;

import javax.annotation.processing.ProcessingEnvironment;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ProcessorUtils {

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

    public static boolean fieldContainsAtLeastOneOfAnnotations(VariableElement field,
                                                               Set<Class<? extends Annotation>> annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            Annotation[] val = field.getAnnotationsByType(annotation);
            if (val.length > 0) {
                return true;
            }
        }
        return false;
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

}
