package validez.examples.dto;

import org.junit.jupiter.api.Test;
import ru.trelloiii.lib.api.Validators;
import ru.trelloiii.lib.exceptions.InvalidException;
import validez.examples.exceptions.CustomNotValidException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonInformationValidatorsTest {

    @Test
    void invalidObject() {
        PersonInformation personInformation = new PersonInformation();
        personInformation.setAge(12);
        personInformation.setName("a");
        personInformation.setSurname("b");
        personInformation.setInn("rwed");
        assertThrows(CustomNotValidException.class, () ->
                Validators.validate(personInformation, CustomNotValidException.class));
    }

}
