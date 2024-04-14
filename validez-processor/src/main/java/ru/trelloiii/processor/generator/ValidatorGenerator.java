package ru.trelloiii.processor.generator;

import com.squareup.javapoet.*;
import ru.trelloiii.lib.annotation.Validator;
import ru.trelloiii.lib.annotation.validators.*;
import ru.trelloiii.processor.config.ConfigProvider;
import ru.trelloiii.processor.generator.fields.*;
import ru.trelloiii.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;

public class ValidatorGenerator {

    private final ProcessingEnvironment processingEnv;
    private final Map<Class<? extends Annotation>, FieldValidator<?>> basicValidators;

    public ValidatorGenerator(ProcessingEnvironment processingEnvironment) {
        this.processingEnv = processingEnvironment;
        basicValidators = Map.of(
                Length.class, new LengthValidator(),
                NotEmpty.class, new NotEmptyValidator(processingEnvironment),
                StringRange.class, new StringRangeValidator(),
                LongRange.class, new LongRangeValidator(),
                IntRange.class, new IntRangeValidator()
        );
    }

    public TypeSpec generateValidator(TypeElement validateClass) {
        List<VariableElement> fields = ProcessorUtils.getFields(validateClass);
        List<ValidField> validFields = filterValidatedFields(fields);
        String validatorName = validateClass.getSimpleName().toString() + "ValidatorImpl";
        String delegateName = "arg0";
        return TypeSpec.classBuilder(validatorName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Validator.class),
                        TypeName.get(validateClass.asType()),
                        ConfigProvider.getExceptionClass()
                ))
                .addMethod(getValidateMethod(validFields, validateClass, delegateName))
                .build();
    }

    private MethodSpec getValidateMethod(List<ValidField> validFields, TypeElement validateClass,
                                         String delegateName) {
        Elements elementUtils = processingEnv.getElementUtils();
        TypeElement validatorInterface = elementUtils.getTypeElement(Validator.class.getCanonicalName());
        ExecutableElement validateSuperMethod = ProcessorUtils.getMethods(validatorInterface)
                .get(0);
        Types typeUtils = processingEnv.getTypeUtils();
        TypeMirror exceptionType = elementUtils.getTypeElement(ConfigProvider.getExceptionClass().toString())
                .asType();
        //get type with generic, as example: Validator<SomeObject>
        DeclaredType interfaceWithGeneric = typeUtils.getDeclaredType(
                validatorInterface,
                validateClass.asType(),
                exceptionType
        );

        MethodSpec.Builder methodBuilder = MethodSpec.overriding(validateSuperMethod,
                interfaceWithGeneric, processingEnv.getTypeUtils());
        for (ValidField validField : validFields) {
            methodBuilder.addCode(validField.createCode(delegateName));
        }
        return methodBuilder.build();
    }

    private List<ValidField> filterValidatedFields(List<VariableElement> allFields) {
        List<ValidField> validFields = new ArrayList<>();
        for (VariableElement field : allFields) {
            boolean hasValidators = ProcessorUtils.fieldContainsAtLeastOneOfAnnotations(
                    field,
                    basicValidators.keySet()
            );
            if (!hasValidators) {
                continue;
            }
            Map<Annotation, FieldValidator<Annotation>> validators = getValidatorsFor(field);
            ValidField validField = new ValidField(processingEnv, field, validators);
            validFields.add(validField);
        }
        return validFields;
    }

    private <T extends Annotation> Map<T, FieldValidator<T>> getValidatorsFor(VariableElement field) {
        Map<T, FieldValidator<T>> validators = new HashMap<>();
        for (Map.Entry<Class<? extends Annotation>, FieldValidator<?>> entry : basicValidators.entrySet()) {
            Class<? extends Annotation> annotationClass = entry.getKey();
            T[] annotations = (T[]) field.getAnnotationsByType(annotationClass);
            if (annotations.length > 0) {
                T annotation = annotations[0];
                FieldValidator<T> fieldValidator = (FieldValidator<T>) entry.getValue();
                validators.put(annotation, fieldValidator);
            }
        }
        return validators;
    }

}
