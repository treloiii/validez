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
     * Annotation class which trigger validation.
     * Will be null, if invalid field type is marked as {@link validez.lib.annotation.Validate}
     */
    @Nullable
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

    /**
     * Will be filled with exception caught by validating property,
     * which type marked as {@link validez.lib.annotation.Validate},
     * otherwise will be null
     */
    @Nullable
    private final Exception cause;

    public ValidatorContext(String name, @Nullable Class<? extends Annotation> annotationClass,
                            @Nullable String property, @Nullable Object fieldValue,
                            @Nullable Exception cause) {
        this.name = name;
        this.annotationClass = annotationClass;
        this.property = property;
        this.fieldValue = fieldValue;
        this.cause = cause;
    }

    public String getName() {
        return name;
    }

    @Nullable
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

    @Nullable
    public Exception getCause() {
        return cause;
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
