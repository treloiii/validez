package validez.examples.dto.validators;

import org.junit.jupiter.api.RepeatedTest;
import validez.examples.exceptions.CustomNotValidException;
import validez.help.ObjectGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LengthObjectTest {

    @RepeatedTest(100)
    void invalidObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        List<LengthObject> invalidObjects =
                generator.generateInvalid(LengthObject.class);
        LengthObjectValidatorImpl validator = new LengthObjectValidatorImpl();
        for (LengthObject invalidObject : invalidObjects) {
            assertThrows(CustomNotValidException.class, () ->
                    validator.validate(invalidObject, null, null));
        }
    }

    @RepeatedTest(100)
    void validObject() throws Exception {
        ObjectGenerator generator = new ObjectGenerator();
        LengthObject validObject = generator.generateValid(LengthObject.class);
        LengthObjectValidatorImpl validator = new LengthObjectValidatorImpl();
        assertDoesNotThrow(() ->
                validator.validate(validObject, null, null));
    }

}
