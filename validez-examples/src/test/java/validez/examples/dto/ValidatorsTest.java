package validez.examples.dto;

import org.junit.jupiter.api.Test;
import validez.examples.dto.validators.ByteBoundObject;
import validez.examples.dto.validators.IntBoundObject;
import validez.examples.dto.validators.LengthObject;
import validez.examples.dto.validators.LongBoundObject;
import validez.examples.dto.validators.MixedValidatorsObject;
import validez.examples.dto.validators.NotEmptyObject;
import validez.examples.dto.validators.RangeValuesObject;
import validez.examples.dto.validators.ShortBoundObject;
import validez.lib.api.Validator;
import validez.lib.api.Validators;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValidatorsTest {

    @Test
    void allValidatorsLoadingTest() {
        assertLoads(CustomValidatedDto.class);
        assertLoads(PaymentData.class);
        assertLoads(PersonInformation.class);
        assertLoads(ByteBoundObject.class);
        assertLoads(IntBoundObject.class);
        assertLoads(LengthObject.class);
        assertLoads(LongBoundObject.class);
        assertLoads(MixedValidatorsObject.class);
        assertLoads(NotEmptyObject.class);
        assertLoads(RangeValuesObject.class);
        assertLoads(ShortBoundObject.class);
    }

    private <T> void assertLoads(Class<T> clazz) {
        Validator<T> validator = Validators.forClass(clazz);
        assertNotNull(validator);
    }

}
