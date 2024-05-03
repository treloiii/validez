package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.examples.exceptions.CustomNotValidException;
import validez.help.ObjectGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShortBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<ShortBoundObject> invalidObjects =
                generator.generateInvalid(ShortBoundObject.class);
        ShortBoundObjectValidatorImpl validator = new ShortBoundObjectValidatorImpl();
        for (ShortBoundObject invalidObject : invalidObjects) {
            assertThrows(CustomNotValidException.class, () ->
                    validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        ShortBoundObject validObject = generator.generateValid(ShortBoundObject.class);
        ShortBoundObjectValidatorImpl validator = new ShortBoundObjectValidatorImpl();
        assertDoesNotThrow(() ->
                validator.validate(validObject, null, null));
    }

}
