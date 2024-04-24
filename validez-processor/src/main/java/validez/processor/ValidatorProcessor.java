package validez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import validez.lib.annotation.ValidatorThrows;
import validez.lib.api.Validators;
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
import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static validez.processor.config.ConfigProvider.VALIDATOR_EXCEPTION;

@SupportedAnnotationTypes("validez.lib.annotation.Validate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ValidatorProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        ConfigProvider.init(processingEnv.getFiler());
        ClassWriter classWriter = new ClassWriter(processingEnv);
        for (TypeElement element : annotations) {
            Set<? extends Element> validateClasses
                    = roundEnv.getElementsAnnotatedWith(element);
            if (validateClasses.isEmpty()) {
                return true;
            }
            Map<TypeElement, JavaFile> validators = new HashMap<>();
            for (Element validateClass : validateClasses) {
                if (validateClass instanceof TypeElement) {
                    try {
                        TypeElement validateClassElement = (TypeElement) validateClass;
                        String onClassException = parseException(validateClassElement);
                        ConfigProvider.override(VALIDATOR_EXCEPTION, onClassException);
                        ValidatorGenerator generator = new ValidatorGenerator(processingEnv);
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
        }
        return true;
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
