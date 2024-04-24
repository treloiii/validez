package validez.processor.generator;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.VariableElement;

public abstract class ValidField {

    private static final String GETTER_PATTERN = "get%s()";

    public abstract CodeBlock createCode(ValidatorArgs args);

    public abstract VariableElement getField();

    protected String generateGetter(VariableElement field) {
        String fieldName = field.getSimpleName().toString();
        fieldName = capitalize(fieldName);
        return GETTER_PATTERN.formatted(fieldName);
    }

    private static String capitalize(String val) {
        byte[] bytes = val.getBytes();
        bytes[0] = (byte) Character.toUpperCase((char) bytes[0]);
        return new String(bytes);
    }

}
