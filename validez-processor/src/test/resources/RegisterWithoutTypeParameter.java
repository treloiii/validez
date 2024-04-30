package test;

import validez.lib.annotation.external.Register;
import validez.lib.api.external.AnnotationProperties;
import validez.lib.api.external.ExternalValidator;

@Register
public class RegisterWithoutTypeParameter implements ExternalValidator {
    @Override
    public boolean validate(AnnotationProperties properties, Object property) {
        return false;
    }
}