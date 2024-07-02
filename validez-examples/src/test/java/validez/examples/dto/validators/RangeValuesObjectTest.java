package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class RangeValuesObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<RangeValuesObject> invalidObjects =
                generator.generateInvalid(RangeValuesObject.class);
        RangeValuesObjectValidatorImpl validator = new RangeValuesObjectValidatorImpl();
        for (RangeValuesObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        RangeValuesObject validObject = generator.generateValid(RangeValuesObject.class);
        RangeValuesObjectValidatorImpl validator = new RangeValuesObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
