package ru.trelloiii.processor.generator.fields;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;

public interface FieldValidator<T extends Annotation> {

    CodeBlock build(T annotation, VariableElement field, String delegateName);

}
