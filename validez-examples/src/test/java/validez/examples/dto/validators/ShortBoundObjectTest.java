package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class ShortBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<ShortBoundObject> invalidObjects =
                generator.generateInvalid(ShortBoundObject.class);
        ShortBoundObjectValidatorImpl validator = new ShortBoundObjectValidatorImpl();
        for (ShortBoundObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        ShortBoundObject validObject = generator.generateValid(ShortBoundObject.class);
        ShortBoundObjectValidatorImpl validator = new ShortBoundObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
