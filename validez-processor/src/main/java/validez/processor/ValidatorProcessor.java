package validez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import validez.lib.annotation.ValidatorThrows;
import validez.lib.api.Validators;
import validez.lib.api.external.ExternalValidator;
import validez.processor.config.ConfigProvider;
import validez.processor.generator.ValidatorGenerator;
import validez.processor.generator.ValidatorsFillerGenerator;
import validez.processor.utils.ClassWriter;
import validez.processor.utils.ProcessorUtils;

import javax.annotation.Nullable;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static validez.processor.config.ConfigProvider.VALIDATOR_EXCEPTION;

@SupportedAnnotationTypes({"validez.lib.annotation.Validate", "validez.lib.annotation.external.Register"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ValidatorProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return true;
        }
        ConfigProvider.init(processingEnv.getFiler());
        ClassWriter classWriter = new ClassWriter(processingEnv);
        Map<String, TypeElement> supportedAnnotations = new HashMap<>();
        for (TypeElement annotation : annotations) {
            supportedAnnotations.put(annotation.getQualifiedName().toString(), annotation);
        }
        TypeElement registerElement
                = supportedAnnotations.get("validez.lib.annotation.external.Register");
        Map<TypeMirror, TypeMirror> registeredPropertyValidators
                = getRegisteredPropertyValidators(registerElement, roundEnv);

        TypeElement validateElement = supportedAnnotations.get("validez.lib.annotation.Validate");
        Set<? extends Element> validateClasses
                = roundEnv.getElementsAnnotatedWith(validateElement);
        if (validateClasses.isEmpty()) {
            return true;
        }
        ValidatorGenerator generator = new ValidatorGenerator(processingEnv, registeredPropertyValidators);
        Map<TypeElement, JavaFile> validators = new HashMap<>();
        for (Element validateClass : validateClasses) {
            if (validateClass instanceof TypeElement) {
                try {
                    TypeElement validateClassElement = (TypeElement) validateClass;
                    String onClassException = parseException(validateClassElement);
                    ConfigProvider.override(VALIDATOR_EXCEPTION, onClassException);
                    TypeSpec validator = generator.generateValidator(validateClassElement);
                    JavaFile validatorSource = classWriter.writeClass(validator, validateClassElement);
                    validators.put(validateClassElement, validatorSource);
                } finally {
                    ConfigProvider.clearOverride(VALIDATOR_EXCEPTION);
                }
            }
        }
        ValidatorsFillerGenerator validatorsFillerGenerator = new ValidatorsFillerGenerator();
        TypeSpec validatorsFiller = validatorsFillerGenerator.generate(validators);
        Elements elementUtils = processingEnv.getElementUtils();
        TypeElement validatorsClass = elementUtils.getTypeElement(Validators.class.getCanonicalName());
        classWriter.writeClass(validatorsFiller, validatorsClass);
        return true;
    }

    private Map<TypeMirror, TypeMirror> getRegisteredPropertyValidators
            (TypeElement registeredAnnotation, RoundEnvironment roundEnvironment) {
        Set<? extends Element> registeredValidators
                = roundEnvironment.getElementsAnnotatedWith(registeredAnnotation);
        if (registeredValidators.isEmpty()) {
            return Collections.emptyMap();
        }
        Types types = processingEnv.getTypeUtils();
        Map<TypeMirror, TypeMirror> registeredPropertyValidators = new HashMap<>();
        for (Element propertyValidatorElement : registeredValidators) {
            TypeElement propertyValidator = (TypeElement) propertyValidatorElement;
            List<? extends TypeMirror> validatorSuperInterfaces = propertyValidator.getInterfaces();
            DeclaredType propertyValidatorInterfaceType = null;
            for (TypeMirror superInterface : validatorSuperInterfaces) {
                String interfaceCanonical = types.erasure(superInterface).toString();
                if (interfaceCanonical.equals(ExternalValidator.class.getCanonicalName())) {
                    propertyValidatorInterfaceType = (DeclaredType) superInterface;
                    break;
                }
            }
            if (propertyValidatorInterfaceType == null) {
                throw new RuntimeException("Class marked with @Register " +
                        "must implement PropertyValidator interface");
            }
            List<? extends TypeMirror> typeParameters
                    = propertyValidatorInterfaceType.getTypeArguments();
            if (typeParameters.isEmpty()) {
                throw new RuntimeException("Class marked with @Register " +
                        "must implement PropertyValidator interface with type parameter");
            }
            TypeMirror annotationType = typeParameters.get(0);
            registeredPropertyValidators.put(annotationType, propertyValidator.asType());
        }
        return registeredPropertyValidators;
    }

    @Nullable
    private String parseException(TypeElement validateClassElement) {
        Elements elements = processingEnv.getElementUtils();
        Object exceptionClass = ProcessorUtils.getAnnotationValue("value", ValidatorThrows.class,
                validateClassElement, elements);
        return Optional.ofNullable(exceptionClass)
                .map(String::valueOf)
                .orElse(null);
    }
}
