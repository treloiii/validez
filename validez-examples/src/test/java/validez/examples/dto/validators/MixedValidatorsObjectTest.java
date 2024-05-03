package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.examples.exceptions.CustomNotValidException;
import validez.help.ObjectGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MixedValidatorsObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<MixedValidatorsObject> invalidObjects =
                generator.generateInvalid(MixedValidatorsObject.class);
        MixedValidatorsObjectValidatorImpl validator = new MixedValidatorsObjectValidatorImpl();
        for (MixedValidatorsObject invalidObject : invalidObjects) {
            assertThrows(CustomNotValidException.class, () ->
                    validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        MixedValidatorsObject validObject = generator.generateValid(MixedValidatorsObject.class);
        MixedValidatorsObjectValidatorImpl validator = new MixedValidatorsObjectValidatorImpl();
        assertDoesNotThrow(() ->
                validator.validate(validObject, null, null));
    }

}
