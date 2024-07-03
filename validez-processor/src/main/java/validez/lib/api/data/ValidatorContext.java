package validez.lib.api.data;

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
 *     <li>sub-validation object validation result</li>
 * </ul>
 */
public class ValidatorContext {

    /**
     * Not valid field name
     */
    private final String fieldName;

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
     * {@link ValidationResult} result for sub-validation (composite) object validation.
     * Will be null, if target validation object has not sub-validation objects
     */
    @Nullable
    private final ValidationResult internalResult;

    public ValidatorContext(String fieldName, @Nullable Class<? extends Annotation> annotationClass,
                            @Nullable String property, @Nullable Object fieldValue) {
        this.fieldName = fieldName;
        this.annotationClass = annotationClass;
        this.property = property;
        this.fieldValue = fieldValue;
        this.internalResult = null;
    }

    public ValidatorContext(String fieldName, @Nullable Class<? extends Annotation> annotationClass,
                            @Nullable String property, @Nullable Object fieldValue,
                            @Nullable ValidationResult internalResult) {
        this.fieldName = fieldName;
        this.annotationClass = annotationClass;
        this.property = property;
        this.fieldValue = fieldValue;
        this.internalResult = internalResult;
    }

    public String getFieldName() {
        return fieldName;
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
    public ValidationResult getInternalResult() {
        return internalResult;
    }

    @Override
    public String toString() {
        return "ValidatorContext{" +
                "name='" + fieldName + '\'' +
                ", annotationClass=" + annotationClass +
                ", property='" + property + '\'' +
                ", fieldValue=" + fieldValue +
                '}';
    }
}
