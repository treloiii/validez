package validez.lib.api.external;

import java.lang.annotation.Annotation;

/**
 * Interface provides API for creating custom annotation-based validators.
 * Implementation must be marked with {@link validez.lib.annotation.external.Register}
 * for registering validator.
 * Registered validators will be found by processor and used for creating validation code using user defined logic.
 * <br>
 * Implementor class must be public with public no args constructor, otherwise compilation will fail.
 * Implementor object will be allocated on each object validation where the corresponding annotation A is presented,
 * so, recommended not make 'fat' implementation for less garbage producing.
 * @param <A> annotation for trigger this interface on field
 */
public interface ExternalValidator<A extends Annotation> {

    /**
     * Method which validate passed field value
     * @param properties annotation metadata
     * @see AnnotationProperties
     * @param property field value
     * @return true - if field value valid, false otherwise
     */
    boolean validate(AnnotationProperties properties, Object property);

}
