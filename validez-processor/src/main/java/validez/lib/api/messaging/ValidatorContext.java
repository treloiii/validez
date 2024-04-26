package validez.lib.api.messaging;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * Object provides meta information about validation then field or invariant not passed condition.
 * It includes information:
 * <ul>
 *     <li>which field is invalid</li>
 *     <li>which annotation is used</li>
 *     <li>which annotation condition not passed</li>
 *     <li>field value</li>
 * </ul>
 * Context will be passed into {@link MessageHandler} during exception message creation.
 */
public class ValidatorContext {

    /**
     * Not valid field name
     */
    private final String name;
    /**
     * Annotation class which trigger validation
     */
    private final Class<? extends Annotation> annotationClass;

    /**
     * Annotation property, which may define validation condition.
     * <br>
     * May be null, if annotation has no property
     */
    @Nullable
    private final String property;
    /**
     * Value of field which is not valid.
     * Will be null, if value not pass not null conditions
     */
    @Nullable
    private final Object fieldValue;

    public ValidatorContext(String name, Class<? extends Annotation> annotationClass,
                            @Nullable String property, @Nullable Object fieldValue) {
        this.name = name;
        this.annotationClass = annotationClass;
        this.property = property;
        this.fieldValue = fieldValue;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    @Nullable
    public String getProperty() {
        return property;
    }

    @Nullable
    public Object getFieldValue() {
        return fieldValue;
    }

    @Override
    public String toString() {
        return "ValidatorContext{" +
                "name='" + name + '\'' +
                ", annotationClass=" + annotationClass +
                ", property='" + property + '\'' +
                ", fieldValue=" + fieldValue +
                '}';
    }
}
