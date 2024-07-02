package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class ByteBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<ByteBoundObject> invalidObjects =
                generator.generateInvalid(ByteBoundObject.class);
        ByteBoundObjectValidatorImpl validator = new ByteBoundObjectValidatorImpl();
        for (ByteBoundObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        ByteBoundObject validObject = generator.generateValid(ByteBoundObject.class);
        ByteBoundObjectValidatorImpl validator = new ByteBoundObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
