package validez.examples.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import validez.examples.exceptions.CustomNotValidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static validez.help.TestUtils.RANDOM;
import static validez.help.TestUtils.randomFrom;
import static validez.help.TestUtils.stringOfLen;
import static validez.help.TestUtils.stringOfRandomLen;

class PersonInformationValidatorsTest {

    public static Stream<Arguments> invalidObjectSource() {
        return Stream.of(
                Arguments.of(
                        PersonInformation.builder()
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(List.of("ST1", "ST2").get(RANDOM.nextInt(0, 1)))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .intStatus(randomFrom(1, 58, 99, 134))
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .intStatus(randomFrom(1, 58, 99, 134))
                                .paymentData(
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
                                )
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .intStatus(randomFrom(1, 58, 99, 134))
                                .paymentData(
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
                                )
                                .partialPaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                                .build()
                                )
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .intStatus(randomFrom(1, 58, 99, 134))
                                .paymentData(
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
                                )
                                .partialPaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                                .build()
                                )
                                .partialExcludePaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .qrCode(stringOfLen(16))
                                                .token(stringOfLen(48))
                                                .paymentType(RANDOM.nextInt(1, 3))
                                                .additionalInfo(UUID.randomUUID().toString())
                                                .build()
                                )
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .intStatus(randomFrom(1, 58, 99, 134))
                                .paymentData(
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
                                )
                                .partialPaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                                .build()
                                )
                                .partialExcludePaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .qrCode(stringOfLen(16))
                                                .token(stringOfLen(48))
                                                .paymentType(RANDOM.nextInt(1, 3))
                                                .additionalInfo(UUID.randomUUID().toString())
                                                .build()
                                )
                                .build()
                ),
                Arguments.of(
                        PersonInformation.builder()
                                .name(stringOfRandomLen(5, 32))
                                .surname(stringOfRandomLen(3, 32))
                                .inn(stringOfLen(16))
                                .age(RANDOM.nextInt(18, 64))
                                .born(RANDOM.nextInt(1982, 2016))
                                .children(List.of(new PersonInformation()))
                                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                                .pseudonym(stringOfRandomLen(3, 150))
                                .status(randomFrom("ST1", "ST2"))
                                .longStatus(randomFrom(123L, 1444L, 893L))
                                .intStatus(randomFrom(1, 58, 99, 134))
                                .paymentData(
                                        PaymentData.builder()
                                                .accountNumber(stringOfLen(32))
                                                .phoneNumber(stringOfLen(11))
                                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                                .cardNumber(stringOfLen(16))
                                                .qrCode(stringOfLen(11))
                                                .token(stringOfLen(2))
                                                .paymentType(RANDOM.nextInt(1, 3))
                                                .additionalInfo(UUID.randomUUID().toString())
                                                .build()
                                )
                                .partialPaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                                .build()
                                )
                                .partialExcludePaymentData(
                                        PaymentData.builder()
                                                .phoneNumber(stringOfLen(11))
                                                .qrCode(stringOfLen(16))
                                                .token(stringOfLen(48))
                                                .paymentType(RANDOM.nextInt(1, 3))
                                                .additionalInfo(UUID.randomUUID().toString())
                                                .build()
                                )
                                .decimalVal(BigDecimal.TEN)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidObjectSource")
    void invalidObject(PersonInformation personInformation) {
        PersonInformationValidatorImpl validator = new PersonInformationValidatorImpl();
        assertThrows(CustomNotValidException.class, () ->
                validator.validate(personInformation, null, null));
    }

    @Test
    void invalidByNullPointer() {
        PersonInformationValidatorImpl validator = new PersonInformationValidatorImpl();
        assertThrows(CustomNotValidException.class, () ->
                validator.validate(null, null, null));
    }

    @Test
    void validObject() {
        PersonInformation personInformation = PersonInformation.builder()
                .name(stringOfRandomLen(5, 32))
                .surname(stringOfRandomLen(3, 32))
                .inn(stringOfLen(16))
                .age(RANDOM.nextInt(18, 64))
                .born(RANDOM.nextInt(1982, 2016))
                .children(List.of(new PersonInformation()))
                .addressLines(Set.of(stringOfRandomLen(10, 15)))
                .pseudonym(stringOfRandomLen(3, 150))
                .status(randomFrom("ST1", "ST2"))
                .longStatus(randomFrom(123L, 1444L, 893L))
                .intStatus(randomFrom(1, 58, 99, 134))
                .paymentData(
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
                )
                .partialPaymentData(
                        PaymentData.builder()
                                .phoneNumber(stringOfLen(11))
                                .pam(stringOfLen(RANDOM.nextInt(3, 1000)))
                                .build()
                )
                .partialExcludePaymentData(
                        PaymentData.builder()
                                .phoneNumber(stringOfLen(11))
                                .qrCode(stringOfLen(16))
                                .token(stringOfLen(48))
                                .paymentType(RANDOM.nextInt(1, 3))
                                .additionalInfo(UUID.randomUUID().toString())
                                .build()
                )
                .decimalVal(BigDecimal.TEN)
                .build();
        PersonInformationValidatorImpl validator = new PersonInformationValidatorImpl();
        assertDoesNotThrow(() ->
                validator.validate(personInformation, null, null));
    }

}
