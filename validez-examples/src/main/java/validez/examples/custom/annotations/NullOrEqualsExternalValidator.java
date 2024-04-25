package validez.examples.custom.annotations;

import validez.lib.annotation.external.Register;
import validez.lib.api.external.AnnotationProperties;
import validez.lib.api.external.ExternalValidator;

@Register
public class NullOrEqualsExternalValidator implements ExternalValidator<NullOrEquals> {

    @Override
    public boolean validate(AnnotationProperties nullOrEquals, Object property) {
        if (property == null) {
            return false;
        }
        if (!(property instanceof Integer)) {
            return false;
        }
        Integer val = (Integer) property;
        return val.equals(nullOrEquals.getValue("eqInt"));
    }

}
