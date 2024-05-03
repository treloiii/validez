package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.examples.exceptions.CustomNotValidException;
import validez.help.ObjectGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LongBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<LongBoundObject> invalidObjects =
                generator.generateInvalid(LongBoundObject.class);
        LongBoundObjectValidatorImpl validator = new LongBoundObjectValidatorImpl();
        for (LongBoundObject invalidObject : invalidObjects) {
            assertThrows(CustomNotValidException.class, () ->
                    validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        LongBoundObject validObject = generator.generateValid(LongBoundObject.class);
        LongBoundObjectValidatorImpl validator = new LongBoundObjectValidatorImpl();
        assertDoesNotThrow(() ->
                validator.validate(validObject, null, null));
    }

}
