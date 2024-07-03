package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class MixedValidatorsObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<MixedValidatorsObject> invalidObjects =
                generator.generateInvalid(MixedValidatorsObject.class);
        MixedValidatorsObjectValidatorImpl validator = new MixedValidatorsObjectValidatorImpl();
        for (MixedValidatorsObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        MixedValidatorsObject validObject = generator.generateValid(MixedValidatorsObject.class);
        MixedValidatorsObjectValidatorImpl validator = new MixedValidatorsObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
