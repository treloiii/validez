package validez.examples.custom.annotations;

import validez.lib.annotation.external.Register;
import validez.lib.api.external.AnnotationProperties;
import validez.lib.api.external.ExternalValidator;

@Register
public class AllPossibleTypesExternalValidator implements ExternalValidator<AllPossibleTypes> {

    public static AnnotationProperties properties;
    public static Object property;

    @Override
    public boolean validate(AnnotationProperties properties, Object property) {
        AllPossibleTypesExternalValidator.property = property;
        AllPossibleTypesExternalValidator.properties = properties;
        return true;
    }
}
