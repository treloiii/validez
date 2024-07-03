package validez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import validez.lib.api.Validators;
import validez.lib.api.external.ExternalValidator;
import validez.processor.generator.ValidatorGenerator;
import validez.processor.generator.ValidatorsFillerGenerator;
import validez.processor.utils.ClassWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
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
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({"validez.lib.annotation.Validate", "validez.lib.annotation.external.Register"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class ValidatorProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        try {
            if (annotations.isEmpty()) {
                return true;
            }
            ClassWriter classWriter = new ClassWriter(processingEnv);
            Map<String, TypeElement> supportedAnnotations = new HashMap<>();
            for (TypeElement annotation : annotations) {
                supportedAnnotations.put(annotation.getQualifiedName().toString(), annotation);
            }

            TypeElement validateElement = supportedAnnotations.get("validez.lib.annotation.Validate");
            if (validateElement == null) {
                return true;
            }
            Set<? extends Element> validateClasses
                    = roundEnv.getElementsAnnotatedWith(validateElement);
            if (validateClasses.isEmpty()) {
                return true;
            }
            TypeElement registerElement
                    = supportedAnnotations.get("validez.lib.annotation.external.Register");
            Map<TypeMirror, TypeMirror> registeredPropertyValidators
                    = getRegisteredPropertyValidators(registerElement, roundEnv);

            Elements elements = processingEnv.getElementUtils();
            ValidatorGenerator generator = new ValidatorGenerator(processingEnv, registeredPropertyValidators);
            Map<TypeElement, JavaFile> validators = new HashMap<>();
            for (Element validateClass : validateClasses) {
                if (validateClass instanceof TypeElement) {
                    TypeElement validateClassElement = (TypeElement) validateClass;
                    boolean hasPackage = classWriter.hasPackage(validateClassElement);
                    if (!hasPackage) {
                        String classType = validateClassElement.asType().toString();
                        throw new RuntimeException("Class %s doesn't have package".formatted(classType));
                    }
                    TypeSpec validator = generator.generateValidator(validateClassElement);
                    JavaFile validatorSource = classWriter.writeClass(validator, validateClassElement);
                    validators.put(validateClassElement, validatorSource);
                }
            }
            ValidatorsFillerGenerator validatorsFillerGenerator = new ValidatorsFillerGenerator();
            TypeSpec validatorsFiller = validatorsFillerGenerator.generate(validators);
            TypeElement validatorsClass = elements.getTypeElement(Validators.class.getCanonicalName());
            classWriter.writeClass(validatorsFiller, validatorsClass);
            return true;
        } catch (Exception e) {
            Messager messager = processingEnv.getMessager();
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }

    private Map<TypeMirror, TypeMirror> getRegisteredPropertyValidators
            (TypeElement registeredAnnotation, RoundEnvironment roundEnvironment) {
        if (registeredAnnotation == null) {
            return Collections.emptyMap();
        }
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
                        "must implement %s interface"
                                .formatted(ExternalValidator.class.getCanonicalName())
                );
            }
            List<? extends TypeMirror> typeParameters
                    = propertyValidatorInterfaceType.getTypeArguments();
            if (typeParameters.isEmpty()) {
                throw new RuntimeException("Class marked with @Register " +
                        "must implement %s interface with type parameter"
                                .formatted(ExternalValidator.class.getCanonicalName())
                );
            }
            TypeMirror annotationType = typeParameters.get(0);
            registeredPropertyValidators.put(annotationType, propertyValidator.asType());
        }
        return registeredPropertyValidators;
    }

}
