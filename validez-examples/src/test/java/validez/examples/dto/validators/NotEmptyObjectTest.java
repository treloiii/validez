package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.help.ObjectGenerator;

import java.util.List;

import static validez.help.TestUtils.assertNotValid;
import static validez.help.TestUtils.assertValid;

class NotEmptyObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<NotEmptyObject> invalidObjects =
                generator.generateInvalid(NotEmptyObject.class);
        NotEmptyObjectValidatorImpl validator = new NotEmptyObjectValidatorImpl();
        for (NotEmptyObject invalidObject : invalidObjects) {
            assertNotValid(() -> validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        NotEmptyObject validObject = generator.generateValid(NotEmptyObject.class);
        NotEmptyObjectValidatorImpl validator = new NotEmptyObjectValidatorImpl();
        assertValid(() -> validator.validate(validObject, null, null));
    }

}
