package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class LengthObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<LengthObject> invalidObjects =
                generator.generateInvalid(LengthObject.class);
        LengthObjectValidatorImpl validator = new LengthObjectValidatorImpl();
        for (LengthObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        LengthObject validObject = generator.generateValid(LengthObject.class);
        LengthObjectValidatorImpl validator = new LengthObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
