package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class IntBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<IntBoundObject> invalidObjects =
                generator.generateInvalid(IntBoundObject.class);
        IntBoundObjectValidatorImpl validator = new IntBoundObjectValidatorImpl();
        for (IntBoundObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        IntBoundObject validObject = generator.generateValid(IntBoundObject.class);
        IntBoundObjectValidatorImpl validator = new IntBoundObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
