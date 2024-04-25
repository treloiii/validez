package validez.examples.dto;

import org.junit.jupiter.api.Test;
import validez.examples.exceptions.CustomNotValidException;
import validez.lib.api.Validators;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomValidatedDtoTest {

    @Test
    void validateCustomDto() {
        CustomValidatedDto dto = new CustomValidatedDto();
        dto.setIntField(0);
        assertThrows(CustomNotValidException.class,
                () -> Validators.validate(dto, CustomNotValidException.class));
    }
}
