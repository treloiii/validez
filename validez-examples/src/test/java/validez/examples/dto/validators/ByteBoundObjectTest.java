package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.examples.exceptions.CustomNotValidException;
import validez.help.ObjectGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteBoundObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<ByteBoundObject> invalidObjects =
                generator.generateInvalid(ByteBoundObject.class);
        ByteBoundObjectValidatorImpl validator = new ByteBoundObjectValidatorImpl();
        for (ByteBoundObject invalidObject : invalidObjects) {
            assertThrows(CustomNotValidException.class, () ->
                    validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        ByteBoundObject validObject = generator.generateValid(ByteBoundObject.class);
        ByteBoundObjectValidatorImpl validator = new ByteBoundObjectValidatorImpl();
        assertDoesNotThrow(() ->
                validator.validate(validObject, null, null));
    }

}
