package validez.examples.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import validez.examples.custom.annotations.AllPossibleTypes;
import validez.examples.custom.annotations.AllPossibleTypesExternalValidator;
import validez.examples.custom.annotations.NullOrEquals;
import validez.examples.exceptions.CustomNotValidException;
import validez.examples.handler.CustomMessageHandler;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NotNull;
import validez.lib.api.external.AnnotationProperties;
import validez.lib.api.messaging.ValidatorContext;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomValidatedDtoTest {

    @BeforeEach
    void nullifyContexts() {
        CustomMessageHandler.handledContext = null;
        CustomMessageHandler.handledInvariant = null;
        CustomMessageHandler.nullHandled = null;
        AllPossibleTypesExternalValidator.properties = null;
        AllPossibleTypesExternalValidator.property = null;
    }


    public static Stream<Arguments> stringValValues() {
        return Stream.of(
                Arguments.of(""),
                () -> new Object[]{null}
        );
    }

    @ParameterizedTest
    @MethodSource("stringValValues")
    void customValidatedDtoInvariantFirstNotValid(String stringValValue) {
        CustomValidatedDto dto = new CustomValidatedDto();
        dto.setIntField(12);
        dto.setStringVal(stringValValue);

        CustomValidatedDtoValidatorImpl validator = new CustomValidatedDtoValidatorImpl();
        assertThrows(CustomNotValidException.class, () ->
                validator.validate(dto, null, null));

        ValidatorContext handledContext = CustomMessageHandler.handledContext;
        Map<String, ValidatorContext> handledInvariant = CustomMessageHandler.handledInvariant;
        assertNull(handledContext);
        assertNotNull(handledInvariant);

        ValidatorContext intField = handledInvariant.get("intField");
        Object intFieldValue = intField.getFieldValue();
        assertEquals(12, (Integer) intFieldValue);
        String nullOrEqualsName = intField.getFieldName();
        assertEquals("intField", nullOrEqualsName);
        Class<? extends Annotation> nullOrEquals = intField.getAnnotationClass();
        assertEquals(NullOrEquals.class, nullOrEquals);
        String nullOrEqualsProperty = intField.getProperty();
        //null because custom validator, not possible handle exactly property
        assertNull(nullOrEqualsProperty);

        ValidatorContext doubleVal = handledInvariant.get("doubleVal");
        //double val is valid, it will be null in members context
        assertNull(doubleVal);

        ValidatorContext stringVal = handledInvariant.get("stringVal");
        assertEquals(stringValValue, stringVal.getFieldValue());
        assertEquals("stringVal", stringVal.getFieldName());
        assertNull(stringVal.getProperty());
        assertEquals(NotEmpty.class, stringVal.getAnnotationClass());

        Object doubleValValue = AllPossibleTypesExternalValidator.property;
        assertEquals(0.00, (double) doubleValValue);
        AnnotationProperties allTypesProperties = AllPossibleTypesExternalValidator.properties;
        assertAllTypesProperties(allTypesProperties);
    }

    private void assertAllTypesProperties(AnnotationProperties properties) {
        assertEquals(30, (int) properties.getValue("integer"));
        assertEquals(34.0, (double) properties.getValue("double_"));
        assertEquals(3333L, (long) properties.getValue("long_"));
        assertEquals(3.45F, (float) properties.getValue("float_"));
        assertFalse((boolean) properties.getValue("boolean_"));
        assertEquals(123, (byte) properties.getValue("byte_"));
        assertArrayEquals(new int[]{1, 2, 3}, properties.getValue("integers"));
        assertArrayEquals(new double[]{3.0, 4.0}, properties.getValue("doubles"));
        assertArrayEquals(new long[]{1L, 2L, 45L, 334L}, properties.getValue("longs"));
        assertArrayEquals(new float[]{0.00f, 0.32f, 1.18f}, properties.getValue("floats"));
        assertArrayEquals(new boolean[]{false, false, true, true}, properties.getValue("booleans"));
        assertArrayEquals(new byte[]{(byte) 34, (byte) 35, (byte) 99}, properties.getValue("bytes"));
        assertEquals(Integer.class, properties.getValue("clazz"));
        assertArrayEquals(new Class<?>[]{String.class, Integer.class}, properties.getValue("classes"));
        assertEquals("default val", properties.getValue("string"));
        assertArrayEquals(new String[]{"one s", "two s", "three s"}, properties.getValue("stringArray"));
        assertEquals(AllPossibleTypes.DumbEnum.TWO, properties.getValue("enum_"));
        assertArrayEquals(new AllPossibleTypes.DumbEnum[]{AllPossibleTypes.DumbEnum.ONE, AllPossibleTypes.DumbEnum.TWO},
                properties.getValue("enumArray"));
        AnnotationProperties annotation = properties.getValue("annotation");
        assertNotNull(annotation);
        assertEquals("is", annotation.getValue("value"));

        AnnotationProperties[] annotationsArray = properties.getValue("annotationsArray");
        assertNotNull(annotationsArray);
        AnnotationProperties arr0 = annotationsArray[0];
        assertEquals("internal value", arr0.getValue("value"));
        AnnotationProperties arr1 = annotationsArray[1];
        assertEquals("not default", arr1.getValue("value"));

        AnnotationProperties[] annotationsArrayOfAnnotations = properties.getValue("annotationsArrayOfAnnotations");
        assertNotNull(annotationsArrayOfAnnotations);

        AnnotationProperties superArr0 = annotationsArrayOfAnnotations[0];
        AnnotationProperties[] superArr0Value = superArr0.getValue("value");
        assertNotNull(superArr0Value);
        AnnotationProperties superArr0ValueProperties0 = superArr0Value[0];
        assertEquals("C", superArr0ValueProperties0.getValue("value"));
        AnnotationProperties superArr0ValueProperties1 = superArr0Value[1];
        assertEquals("D", superArr0ValueProperties1.getValue("value"));


        AnnotationProperties superArr1 = annotationsArrayOfAnnotations[1];
        AnnotationProperties[] superArr1Value = superArr1.getValue("value");
        assertNotNull(superArr1Value);
        AnnotationProperties superArr1ValueProperties0 = superArr1Value[0];
        assertEquals("A", superArr1ValueProperties0.getValue("value"));
        AnnotationProperties superArr1ValueProperties1 = superArr1Value[1];
        assertEquals("B", superArr1ValueProperties1.getValue("value"));
    }

    @Test
    void customValidatedDtoInvariantValidString2ValNotValid() {
        CustomValidatedDto dto = new CustomValidatedDto();
        dto.setIntField(152); //valid

        CustomValidatedDtoValidatorImpl validator = new CustomValidatedDtoValidatorImpl();
        assertThrows(CustomNotValidException.class, () ->
                validator.validate(dto, null, null));

        ValidatorContext handledContext = CustomMessageHandler.handledContext;
        Map<String, ValidatorContext> handledInvariant = CustomMessageHandler.handledInvariant;
        assertNotNull(handledContext);
        assertNull(handledInvariant);

        assertNull(handledContext.getFieldValue());
        assertEquals("stringVal2", handledContext.getFieldName());
        assertNull(handledContext.getProperty());
        assertEquals(NotNull.class, handledContext.getAnnotationClass());
    }

    public static Stream<Arguments> validObjectSource() {
        return Stream.of(
                Arguments.of(new CustomValidatedDto(152, 0, "s", "")),
                Arguments.of(new CustomValidatedDto(152, 234, UUID.randomUUID().toString(), UUID.randomUUID().toString())),
                Arguments.of(new CustomValidatedDto(null, 234, UUID.randomUUID().toString(), UUID.randomUUID().toString())),
                Arguments.of(new CustomValidatedDto(12, 234, UUID.randomUUID().toString(), UUID.randomUUID().toString()))

        );
    }

    @ParameterizedTest
    @MethodSource("validObjectSource")
    void validObject(CustomValidatedDto dto) {
        CustomValidatedDtoValidatorImpl validator = new CustomValidatedDtoValidatorImpl();
        assertDoesNotThrow(() -> validator.validate(dto, null, null));
    }

    @Test
    void invalidByNullPointer() {
        CustomValidatedDtoValidatorImpl validator = new CustomValidatedDtoValidatorImpl();
        assertThrows(CustomNotValidException.class, () ->
                validator.validate(null, null, null));
        assertTrue(CustomMessageHandler.nullHandled);
    }
}
