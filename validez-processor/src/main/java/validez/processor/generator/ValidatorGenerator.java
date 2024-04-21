package validez.processor.generator;

import com.squareup.javapoet.*;
import validez.lib.annotation.Validate;
import validez.lib.annotation.Validator;
import validez.lib.annotation.conditions.Exclude;
import validez.lib.annotation.messaging.ModifyMessage;
import validez.lib.annotation.validators.*;
import validez.lib.api.messaging.DefaultMessageHandler;
import validez.lib.api.messaging.MessageHandler;
import validez.processor.config.ConfigProvider;
import validez.processor.generator.fields.*;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;

import static validez.processor.generator.ValidatorArgs.VALIDATE_ARGS;
import static validez.processor.utils.ProcessorUtils.elementContainsAtLeastOneOfAnnotations;
import static validez.processor.utils.ProcessorUtils.getAnnotationValue;

public class ValidatorGenerator {

    private final ProcessingEnvironment processingEnv;
    private final Map<Class<? extends Annotation>, FieldValidator<?>> basicValidators;

    public ValidatorGenerator(ProcessingEnvironment processingEnvironment) {
        this.processingEnv = processingEnvironment;
        basicValidators = Map.of(
                Length.class, new LengthValidator(processingEnvironment),
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
        return TypeSpec.classBuilder(validatorName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Validator.class),
                        TypeName.get(validateClass.asType()),
                        ConfigProvider.getExceptionClass()
                ))
                .addMethod(getValidateMethod(validFields, validateClass))
                .build();
    }

    private MethodSpec getValidateMethod(List<ValidField> validFields, TypeElement validateClass) {
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
                interfaceWithGeneric, typeUtils);
        String handlerClass = getAnnotationValue("messageHandler", ModifyMessage.class,
                validateClass, elementUtils);
        ClassName handlerClassName;
        if (handlerClass != null) {
            handlerClass = handlerClass.substring(0, handlerClass.length() - ".class".length());
            handlerClassName = ClassName.bestGuess(handlerClass);
        } else {
            handlerClassName = ClassName.get(DefaultMessageHandler.class);
        }
        CodeBlock handlerInitCode = CodeBlock.builder()
                .addStatement("$T messageHandler_ = new $T()", handlerClassName, handlerClassName)
                .build();
        methodBuilder.addCode(handlerInitCode);
        for (ValidField validField : validFields) {
            methodBuilder.addCode(validField.createCode(VALIDATE_ARGS));
        }
        return renameParameters(methodBuilder.build(), VALIDATE_ARGS.args());
    }

    private MethodSpec renameParameters(MethodSpec source, String... names) {
        if (names.length == 0) {
            return source;
        }
        if (names.length != source.parameters.size()) {
            throw new IllegalArgumentException("Rename length != parameters length");
        }
        MethodSpec.Builder builder = MethodSpec.methodBuilder(source.name)
                .returns(source.returnType)
                .addAnnotations(source.annotations)
                .addExceptions(source.exceptions)
                .addModifiers(source.modifiers)
                .addCode(source.code);
        int index = 0;
        for (ParameterSpec parameter : source.parameters) {
            ParameterSpec renamed = ParameterSpec.builder(parameter.type, names[index])
                    .addModifiers(parameter.modifiers)
                    .addAnnotations(parameter.annotations)
                    .addJavadoc(parameter.javadoc)
                    .build();
            builder.addParameter(renamed);
            index++;
        }
        return builder.build();
    }

    private List<ValidField> filterValidatedFields(List<VariableElement> allFields) {
        List<ValidField> validFields = new ArrayList<>();
        for (VariableElement field : allFields) {
            boolean excluded = elementContainsAtLeastOneOfAnnotations(field, Set.of(Exclude.class));
            if (excluded) {
                continue;
            }
            boolean hasValidators = elementContainsAtLeastOneOfAnnotations(
                    field,
                    basicValidators.keySet()
            );
            if (!hasValidators) {
                TypeMirror type = field.asType();
                Types typeUtils = processingEnv.getTypeUtils();
                Element element = typeUtils.asElement(type);
                boolean complex = elementContainsAtLeastOneOfAnnotations(element, Set.of(Validate.class));
                if (complex) {
                    ComplexField complexField = new ComplexField(field);
                    validFields.add(complexField);
                }
            } else {
                Map<Annotation, FieldValidator<Annotation>> validators = getValidatorsFor(field);
                SimpleField simpleField = new SimpleField(field, validators);
                validFields.add(simpleField);
            }
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
