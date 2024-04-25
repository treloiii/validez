package validez.examples.custom.annotations;

import validez.lib.annotation.external.Register;
import validez.lib.api.external.AnnotationProperties;
import validez.lib.api.external.ExternalValidator;

@Register
public class AllPossibleTypesExternalValidator implements ExternalValidator<AllPossibleTypes> {
    @Override
    public boolean validate(AnnotationProperties properties, Object property) {
        //always valid, just for check
        return true;
    }
}
