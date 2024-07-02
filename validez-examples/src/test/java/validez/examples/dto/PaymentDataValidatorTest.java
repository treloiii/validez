package validez.examples.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import validez.lib.api.data.ValidationResult;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static validez.help.TestUtils.stringOfLen;

class PaymentDataValidatorTest {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static Stream<Arguments> invalidSource() {
        return Stream.of(
                Arguments.of(ofRecipientInfo(null, null, null, null)),
                Arguments.of(ofRecipientInfo(stringOfLen(12), null, null, null)),
                Arguments.of(ofRecipientInfo(stringOfLen(9), "", null, null)),
                Arguments.of(ofRecipientInfo("", "", null, null)),
                Arguments.of(ofRecipientInfo(stringOfLen(12), stringOfLen(2), null, null)),
                Arguments.of(ofRecipientInfo(stringOfLen(43), "", stringOfLen(4), null)),
                Arguments.of(ofRecipientInfo(stringOfLen(43), "", "", null)),
                Arguments.of(ofRecipientInfo(stringOfLen(43), "", stringOfLen(9), stringOfLen(14))),
                Arguments.of(ofRecipientInfo(stringOfLen(43), "", stringOfLen(9), "")),
                Arguments.of(ofTransactionData(null, null)),
                Arguments.of(ofTransactionData(stringOfLen(4), null)),
                Arguments.of(ofTransactionData(null, stringOfLen(9))),
                Arguments.of(ofTransactionData("", "")),
                Arguments.of(ofOthers(RANDOM.nextInt(4, 1000))),
                Arguments.of(ofOthers(0)),
                Arguments.of(ofOthers(2))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidSource")
    void objectInvalid(PaymentData paymentData) {
        PaymentDataValidatorImpl validator = new PaymentDataValidatorImpl();
        ValidationResult result = validator.validate(paymentData, null, null);
        assertFalse(result.isValid());
    }

    @Test
    void invalidByNullPointer() {
        PaymentDataValidatorImpl validator = new PaymentDataValidatorImpl();
        ValidationResult result = validator.validate(null, null, null);
        assertFalse(result.isValid());
    }

    public static Stream<Arguments> validSource() {
        return Stream.of(
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(32))
                                .phoneNumber(stringOfLen(11))
                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                ),
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(10))
                                .phoneNumber(stringOfLen(11))
                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                ),
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(10))
                                .phoneNumber(stringOfLen(3))
                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                ),
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(10))
                                .phoneNumber(stringOfLen(3))
                                .pam(stringOfLen(1))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                ),
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(10))
                                .phoneNumber(stringOfLen(3))
                                .pam(stringOfLen(1))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(1))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                ),
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(10))
                                .phoneNumber(stringOfLen(3))
                                .pam(stringOfLen(1))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(2))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                ),
                Arguments.of(
                        PaymentData.builder()
                                .accountNumber(stringOfLen(32))
                                .phoneNumber(stringOfLen(1))
                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                .cardNumber(stringOfLen(16))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validSource")
    void objectValid(PaymentData paymentData) {
        PaymentDataValidatorImpl validator = new PaymentDataValidatorImpl();
        ValidationResult result = validator.validate(paymentData, null, null);
        assertTrue(result.isValid());
    }

    private static PaymentData ofTransactionData(String qrCode, String token) {
        return new PaymentData(
                stringOfLen(32), stringOfLen(11), stringOfLen(8), stringOfLen(16),
                qrCode, token, 3, stringOfLen(99)
        );
    }

    private static PaymentData ofRecipientInfo(String phoneNumber,
                                               String pam,
                                               String accountNumber,
                                               String cardNumber) {
        return new PaymentData(
                accountNumber, phoneNumber, pam, cardNumber,
                stringOfLen(16), stringOfLen(48), 1, stringOfLen(45)
        );
    }

    private static PaymentData ofOthers(int paymentType) {
        return new PaymentData(
                stringOfLen(32), stringOfLen(11), stringOfLen(8), stringOfLen(16),
                stringOfLen(16), stringOfLen(48), paymentType, null
        );
    }

}
