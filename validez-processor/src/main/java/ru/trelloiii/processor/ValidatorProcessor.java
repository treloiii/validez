package ru.trelloiii.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import ru.trelloiii.lib.api.Validators;
import ru.trelloiii.processor.config.ConfigProvider;
import ru.trelloiii.processor.generator.ValidatorGenerator;
import ru.trelloiii.processor.generator.ValidatorsFillerGenerator;
import ru.trelloiii.processor.utils.ClassWriter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("ru.trelloiii.lib.annotation.Validate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ValidatorProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        ConfigProvider.init(processingEnv.getFiler());
        ClassWriter classWriter = new ClassWriter(processingEnv);
        for (TypeElement element: annotations) {
            Set<? extends Element> validateClasses
                    = roundEnv.getElementsAnnotatedWith(element);
            if (validateClasses.isEmpty()) {
                return true;
            }
            Map<TypeElement, JavaFile> validators = new HashMap<>();
            for (Element validateClass : validateClasses) {
                if (validateClass instanceof TypeElement) {
                    ValidatorGenerator generator = new ValidatorGenerator(processingEnv);
                    TypeElement validateClassElement = (TypeElement) validateClass;
                    TypeSpec validator = generator.generateValidator(validateClassElement);
                    JavaFile validatorSource = classWriter.writeClass(validator, validateClassElement);
                    validators.put(validateClassElement, validatorSource);
                }
            }
            ValidatorsFillerGenerator  validatorsFillerGenerator = new ValidatorsFillerGenerator();
            TypeSpec validatorsFiller = validatorsFillerGenerator.generate(validators);
            Elements elementUtils = processingEnv.getElementUtils();
            TypeElement validatorsClass = elementUtils.getTypeElement(Validators.class.getCanonicalName());
            classWriter.writeClass(validatorsFiller, validatorsClass);
        }
        return true;
    }
}
