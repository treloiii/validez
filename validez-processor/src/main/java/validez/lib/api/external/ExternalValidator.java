package validez.lib.api.external;

import java.lang.annotation.Annotation;

public interface ExternalValidator<A extends Annotation> {

    boolean validate(AnnotationProperties properties, Object property);

}
