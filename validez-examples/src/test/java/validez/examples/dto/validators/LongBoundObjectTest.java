package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class LongBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<LongBoundObject> invalidObjects =
                generator.generateInvalid(LongBoundObject.class);
        LongBoundObjectValidatorImpl validator = new LongBoundObjectValidatorImpl();
        for (LongBoundObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        LongBoundObject validObject = generator.generateValid(LongBoundObject.class);
        LongBoundObjectValidatorImpl validator = new LongBoundObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
