package validez.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Exclude;
import validez.lib.annotation.conditions.Fields;
import validez.lib.annotation.conditions.Invariant;
import validez.lib.annotation.conditions.Invariants;
import validez.lib.annotation.messaging.ModifyMessage;
import validez.lib.annotation.validators.ByteBound;
import validez.lib.annotation.validators.IntBound;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.LongBound;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NotNull;
import validez.lib.annotation.validators.ShortBound;
import validez.lib.annotation.validators.StringRange;
import validez.lib.api.Validator;
import validez.lib.api.messaging.DefaultMessageHandler;
import validez.lib.api.messaging.ValidatorContext;
import validez.processor.generator.fields.ByteBoundValidator;
import validez.processor.generator.fields.FieldValidator;
import validez.processor.generator.fields.IntBoundValidator;
import validez.processor.generator.fields.IntRangeValidator;
import validez.processor.generator.fields.LengthValidator;
import validez.processor.generator.fields.LongBoundValidator;
import validez.processor.generator.fields.LongRangeValidator;
import validez.processor.generator.fields.NotEmptyValidator;
import validez.processor.generator.fields.NotNullValidator;
import validez.processor.generator.fields.ShortBoundValidator;
import validez.processor.generator.fields.StringRangeValidator;
import validez.processor.generator.fields.external.ExternalAnnotationValidator;
import validez.processor.generator.fields.external.ExternalDefinedAnnotationValidator;
import validez.processor.generator.help.AnnotationAndValidator;
import validez.processor.generator.help.InvariantFields;
import validez.processor.generator.help.InvariantHolder;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static validez.processor.config.ConfigProvider.getExceptionClass;
import static validez.processor.generator.ValidatorArgs.VALIDATE_ARGS;
import static validez.processor.utils.CodeUtils.throwWithContext;
import static validez.processor.utils.CodeUtils.throwWithContextInvariant;
import static validez.processor.utils.ProcessorUtils.createGenerated;
import static validez.processor.utils.ProcessorUtils.getAnnotationValue;
import static validez.processor.utils.ProcessorUtils.getAnnotationsOfType;
import static validez.processor.utils.ProcessorUtils.getAnnotationsValues;
import static validez.processor.utils.ProcessorUtils.getAnnotationsValuesFromMirrors;

public class ValidatorGenerator {

    private final ProcessingEnvironment processingEnv;
    private final Map<Class<? extends Annotation>, FieldValidator<?>> basicValidators;

    private final ExternalAnnotationValidator externalValidator;
    private final Map<TypeMirror, TypeMirror> registeredPropertyValidators;

    public ValidatorGenerator(ProcessingEnvironment processingEnvironment,
                              Map<TypeMirror, TypeMirror> registeredPropertyValidators) {
        this.processingEnv = processingEnvironment;
        basicValidators = Map.of(
                Length.class, new LengthValidator(processingEnvironment),
                NotEmpty.class, new NotEmptyValidator(processingEnvironment),
                StringRange.class, new StringRangeValidator(processingEnvironment),
                LongRange.class, new LongRangeValidator(processingEnvironment),
                IntRange.class, new IntRangeValidator(processingEnvironment),
                NotNull.class, new NotNullValidator(),
                IntBound.class, new IntBoundValidator(processingEnvironment),
                LongBound.class, new LongBoundValidator(processingEnvironment),
                ShortBound.class, new ShortBoundValidator(processingEnvironment),
                ByteBound.class, new ByteBoundValidator(processingEnvironment)
        );
        this.registeredPropertyValidators = registeredPropertyValidators;
        this.externalValidator = new ExternalDefinedAnnotationValidator(processingEnv);
    }

    public TypeSpec generateValidator(TypeElement validateClass) {
        List<VariableElement> fields = ProcessorUtils.getFields(validateClass);
        List<InvariantHolder> invariants = getInvariants(validateClass);
        Map<String, ValidField> validFields = filterValidatedFields(fields);
        String validatorName = validateClass.getSimpleName().toString() + "ValidatorImpl";
        TypeSpec.Builder generatorBuilder = TypeSpec.classBuilder(validatorName)
                .addAnnotation(createGenerated(ValidatorGenerator.class))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Validator.class),
                        TypeName.get(validateClass.asType()),
                        getExceptionClass()
                ));
        generatorBuilder.addMethod(getValidateMethod(validFields, invariants, validateClass, generatorBuilder));
        return generatorBuilder.build();
    }

    private MethodSpec getValidateMethod(Map<String, ValidField> validFields,
                                         List<InvariantHolder> invariants,
                                         TypeElement validateClass,
                                         TypeSpec.Builder generatorBuilder) {
        Elements elementUtils = processingEnv.getElementUtils();
        TypeElement validatorInterface = elementUtils.getTypeElement(Validator.class.getCanonicalName());
        ExecutableElement validateSuperMethod = ProcessorUtils.getMethods(validatorInterface)
                .get(0);
        Types typeUtils = processingEnv.getTypeUtils();
        TypeMirror exceptionType = elementUtils.getTypeElement(getExceptionClass().toString())
                .asType();
        //get type with generic, as example: Validator<SomeObject>
        DeclaredType interfaceWithGeneric = typeUtils.getDeclaredType(
                validatorInterface,
                validateClass.asType(),
                exceptionType
        );
        MethodSpec.Builder methodBuilder = MethodSpec.overriding(validateSuperMethod,
                interfaceWithGeneric, typeUtils);
        Object handlerClass = getAnnotationValue("messageHandler", ModifyMessage.class,
                validateClass, elementUtils);
        ClassName handlerClassName;
        if (handlerClass != null) {
            String handlerClassString = handlerClass.toString();
            handlerClassName = ClassName.bestGuess(handlerClassString);
        } else {
            handlerClassName = ClassName.get(DefaultMessageHandler.class);
        }
        CodeBlock handlerInitCode = CodeBlock.builder()
                .addStatement("$T $N = new $T()",
                        handlerClassName, VALIDATE_ARGS.getMessageHandlerName(), handlerClassName)
                .build();
        methodBuilder.addCode(handlerInitCode);
        methodBuilder.addCode(
                CodeBlock.builder()
                        .beginControlFlow("if ($N == null)", VALIDATE_ARGS.getDelegateName())
                        .addStatement("throw new $T($N.handleNull())", getExceptionClass(), VALIDATE_ARGS.getMessageHandlerName())
                        .endControlFlow()
                        .build()
        );

        for (InvariantHolder invariant : invariants) {
            String invariantName = invariant.getName();
            List<InvariantFields> invariantFields = invariant.getInvariantFields();
            if (invariantFields.size() <= 1) {
                continue;
            }
            methodBuilder.addComment("handle invariant $N", invariantName);
            Map<String, String> fieldToContext = new LinkedHashMap<>();
            for (InvariantFields invariantField : invariantFields) {
                List<String> invariantFieldNames = invariantField.getFields();
                List<String> contexts = new ArrayList<>();
                for (String fieldName : invariantFieldNames) {
                    ValidField validField = validFields.get(fieldName);
                    if (validField == null) {
                        throw new RuntimeException("Field %s from invariant %s not exists or duplicates"
                                .formatted(fieldName, invariantName));
                    }
                    MethodSpec validateFieldMethod = validateFieldMethod(validField, validateClass);
                    generatorBuilder.addMethod(validateFieldMethod);
                    String contextName = CodeBlock.of("$$$NContext", fieldName)
                            .toString();
                    methodBuilder.addCode(
                            CodeBlock.builder()
                                    .add(validateFieldStatement(contextName, validateFieldMethod))
                                    .build()
                    );
                    fieldToContext.put(fieldName, contextName);
                    contexts.add(
                            CodeBlock.of("$N != null", contextName).toString()
                    );
                    validFields.remove(fieldName);
                }
                String ifContextCase = String.join("||", contexts);
                methodBuilder
                        .beginControlFlow("if ($L)", ifContextCase);
            }
            methodBuilder.addCode(throwWithContextInvariant(invariantName, VALIDATE_ARGS, fieldToContext));
            invariantFields.forEach(_unused -> methodBuilder.endControlFlow());
        }

        for (ValidField validField : validFields.values()) {
            Name fieldName = validField.getField().getSimpleName();
            MethodSpec validateFieldMethod = validateFieldMethod(validField, validateClass);
            generatorBuilder.addMethod(validateFieldMethod);
            String contextName = CodeBlock.of("$$$NContext", fieldName)
                    .toString();
            methodBuilder.addCode(
                    CodeBlock.builder()
                            .add(validateFieldStatement(contextName, validateFieldMethod))
                            .beginControlFlow("if ($N != null)", contextName)
                            .add(throwWithContext(contextName, VALIDATE_ARGS))
                            .endControlFlow()
                            .build()
            );
        }
        return renameParameters(methodBuilder.build(), VALIDATE_ARGS.args());
    }

    private CodeBlock validateFieldStatement(String contextName, MethodSpec validateFieldMethod) {
        return CodeBlock.builder()
                .addStatement("$T $L = this.$N($L, $L, $L)",
                        ValidatorContext.class,
                        contextName,
                        validateFieldMethod,
                        VALIDATE_ARGS.getDelegateName(),
                        VALIDATE_ARGS.getIncludesName(),
                        VALIDATE_ARGS.getExcludesName()
                )
                .build();
    }

    private MethodSpec validateFieldMethod(ValidField field, TypeElement validateClass) {
        VariableElement variableElement = field.getField();
        Name fieldName = variableElement.getSimpleName();
        TypeName validatedObjectClass = TypeName.get(validateClass.asType());
        TypeName stringSetType = ParameterizedTypeName.get(Set.class, String.class);
        return MethodSpec.methodBuilder("validate$" + fieldName)
                .addModifiers(Modifier.PRIVATE)
                .returns(ClassName.get(ValidatorContext.class))
                .addParameter(
                        ParameterSpec.builder(validatedObjectClass, VALIDATE_ARGS.getDelegateName())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(stringSetType, VALIDATE_ARGS.getIncludesName())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(stringSetType, VALIDATE_ARGS.getExcludesName())
                                .build()
                )
                .addCode(field.createCode(VALIDATE_ARGS))
                .addException(getExceptionClass())
                .build();
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

    private List<InvariantHolder> getInvariants(TypeElement validateClass) {
        Elements elements = processingEnv.getElementUtils();
        List<Object> values = getAnnotationsValues("value", Invariants.class, validateClass, elements);
        List<InvariantHolder> invariants = new ArrayList<>();
        if (values.isEmpty()) {
            AnnotationMirror invariant = getAnnotationsOfType(Invariant.class, validateClass, elements);
            if (invariant != null) {
                invariants.add(getInvariant(invariant));
            }
            return invariants;
        }
        for (Object value : values) {
            for (AnnotationMirror valueMirror : (List<AnnotationMirror>) value) {
                invariants.add(getInvariant(valueMirror));
            }
        }
        return invariants;
    }

    private InvariantHolder getInvariant(AnnotationMirror invariantAnnotation) {
        InvariantHolder invariant = new InvariantHolder();
        Map<? extends ExecutableElement, ? extends AnnotationValue> properties
                = invariantAnnotation.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> property : properties.entrySet()) {
            ExecutableElement propertyMethod = property.getKey();
            String propertyName = propertyMethod.getSimpleName().toString();
            AnnotationValue propertyAnnotationValue = property.getValue();
            String propertyValue = propertyAnnotationValue.getValue().toString();
            if ("name".equals(propertyName)) {
                invariant.setName(propertyValue);
            } else {
                invariant.setInvariantFields(getInvariantFields(propertyAnnotationValue));
            }
        }
        return invariant;
    }

    private List<InvariantFields> getInvariantFields(AnnotationValue fieldAnnotations) {
        List<AnnotationMirror> fields = (List<AnnotationMirror>) fieldAnnotations.getValue();
        Elements elements = processingEnv.getElementUtils();
        return getAnnotationsValuesFromMirrors("value", Fields.class, fields, elements)
                .stream()
                .map(v -> (List<AnnotationValue>) v)
                .map(annotationValues -> annotationValues.stream()
                        .map(AnnotationValue::getValue)
                        .map(String::valueOf)
                        .collect(Collectors.toList()))
                .map(InvariantFields::new)
                .collect(Collectors.toList());
    }

    private Map<String, ValidField> filterValidatedFields(List<VariableElement> allFields) {
        Map<String, ValidField> validFields = new LinkedHashMap<>();
        Types types = processingEnv.getTypeUtils();
        Elements elements = processingEnv.getElementUtils();
        for (VariableElement field : allFields) {
            Exclude exclude = field.getAnnotation(Exclude.class);
            if (exclude == null) {
                String fieldName = field.getSimpleName().toString();
                Map<Annotation, FieldValidator<Annotation>> fieldValidators = getValidatorsFor(field);
                List<AnnotationAndValidator> externalValidators = getExternalValidatorsFor(field);
                if (fieldValidators.isEmpty() && externalValidators.isEmpty()) {
                    TypeMirror fieldType = field.asType();
                    TypeKind fieldTypeKind = fieldType.getKind();
                    if (!fieldTypeKind.isPrimitive()) {
                        Element fieldElement = types.asElement(fieldType);
                        Validate validate = fieldElement.getAnnotation(Validate.class);
                        if (validate != null) {
                            ComplexField complexField = new ComplexField(types, elements, field);
                            validFields.put(fieldName, complexField);
                        }
                    }
                } else {
                    SimpleField simpleField = new SimpleField(field, fieldValidators,
                            externalValidator, externalValidators);
                    validFields.put(fieldName, simpleField);
                }
            }
        }
        return validFields;
    }

    private List<AnnotationAndValidator> getExternalValidatorsFor(VariableElement field) {
        if (registeredPropertyValidators.isEmpty()) {
            return Collections.emptyList();
        }
        List<? extends AnnotationMirror> fieldAnnotations = field.getAnnotationMirrors();
        List<AnnotationAndValidator> externalValidators = new ArrayList<>();
        for (AnnotationMirror fieldAnnotation : fieldAnnotations) {
            DeclaredType annotationType = fieldAnnotation.getAnnotationType();
            TypeMirror validatorType = registeredPropertyValidators.get(annotationType);
            if (validatorType != null) {
                externalValidators.add(new AnnotationAndValidator(fieldAnnotation, validatorType));
            }
        }
        return externalValidators;
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
