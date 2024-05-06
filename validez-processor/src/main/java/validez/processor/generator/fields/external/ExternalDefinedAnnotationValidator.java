package validez.processor.generator.fields.external;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.RequiredArgsConstructor;
import validez.lib.api.external.AnnotationProperties;
import validez.processor.generator.ValidatorArgs;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static validez.processor.utils.CodeUtils.initializeArray;
import static validez.processor.utils.CodeUtils.returnValidatorContext;

@RequiredArgsConstructor
public class ExternalDefinedAnnotationValidator implements ExternalAnnotationValidator {

    private final ProcessingEnvironment processingEnvironment;

    @Override
    public CodeBlock build(VariableElement field, AnnotationMirror annotation,
                           TypeMirror externalValidatorType, ValidatorArgs args) {
        String validatorName = "$$$customValidator";
        Name fieldName = field.getSimpleName();
        String propertiesName = CodeBlock.of("$$$NProperties", fieldName)
                .toString();
        ClassName annotationClass = (ClassName) ClassName.get(annotation.getAnnotationType());
        return CodeBlock.builder()
                .addStatement("$T $N = new $T()", externalValidatorType, validatorName, externalValidatorType)
                .add(parseAnnotationProperties(annotation, propertiesName))
                .addStatement("boolean valid = $N.validate($N, $N)", validatorName, propertiesName, fieldName)
                .beginControlFlow("if (!valid)")
                .addStatement(returnValidatorContext(fieldName, null, annotationClass))
                .endControlFlow()
                .build();
    }


    private CodeBlock parseAnnotationProperties(AnnotationMirror annotation, String propertiesName) {
        Elements elements = processingEnvironment.getElementUtils();
        Map<? extends ExecutableElement, ? extends AnnotationValue> values
                = elements.getElementValuesWithDefaults(annotation);
        CodeBlock.Builder propertiesInit = CodeBlock.builder();
        String mapName = propertiesName + "Map";
        TypeName mapType = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        propertiesInit.addStatement("$T $N = new $T()", mapType, mapName, HashMap.class);
        int annotationOrder = 0;
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
            ExecutableElement propertyExecutable = entry.getKey();
            String property = propertyExecutable.getSimpleName().toString();
            AnnotationValue annotationValue = entry.getValue();
            Object objectValue = annotationValue.getValue();
            if (objectValue instanceof List) {
                List<?> listValue = (List<?>) objectValue;
                CodeBlock.Builder arrayValuesInitBuilder = CodeBlock.builder();
                String arrayName = CodeBlock.of("$$$NVal$L", propertiesName + property, annotationOrder)
                        .toString();
                int iteration = 0;
                for (Object listElement : listValue) {
                    if (listElement instanceof AnnotationValue) {
                        //array element annotation handle
                        AnnotationValue listAnnotationValueElement = (AnnotationValue) listElement;
                        Object listElementValue = listAnnotationValueElement.getValue();
                        if (listElementValue instanceof AnnotationMirror) {
                            if (iteration == 0) {
                                arrayValuesInitBuilder.add(
                                        createArray(AnnotationProperties.class, arrayName, listValue)
                                );
                            }
                            AnnotationMirror annotationElementValue = (AnnotationMirror) listElementValue;
                            String deepPropertiesName = "_" + annotationOrder + iteration + propertiesName;
                            CodeBlock internalProps
                                    = parseAnnotationProperties(annotationElementValue, deepPropertiesName);
                            arrayValuesInitBuilder.add(internalProps);
                            arrayValuesInitBuilder.addStatement("$N[$L] = $L",
                                    arrayName, iteration, deepPropertiesName);
                        } else if (listElementValue instanceof VariableElement) {
                            //array element enum value handle
                            VariableElement enumElement = (VariableElement) listElementValue;
                            EnumHolder enumHolder = new EnumHolder(enumElement);
                            CodeBlock enumConstant = enumHolder.getEnumConstant();
                            if (iteration == 0) {
                                arrayValuesInitBuilder.add(
                                        createArray(enumHolder.getEnumClass(), arrayName, listValue)
                                );
                            }
                            arrayValuesInitBuilder.addStatement("$N[$L] = $L",
                                    arrayName, iteration, enumConstant);
                        } else if (listElementValue instanceof String) {
                            //array element string value handle
                            String stringElementValue = (String) listElementValue;
                            if (iteration == 0) {
                                arrayValuesInitBuilder.add(
                                        createArray(String.class, arrayName, listValue)
                                );
                            }
                            arrayValuesInitBuilder.addStatement("$N[$L] = $S",
                                    arrayName, iteration, stringElementValue);
                        } else if (listElementValue instanceof TypeMirror) {
                            //array element class value handle
                            if (iteration == 0) {
                                arrayValuesInitBuilder.add(
                                        createArray(Class.class, arrayName, listValue)
                                );
                            }
                            TypeName classElementValue = ClassName.get((TypeMirror) listElementValue);
                            arrayValuesInitBuilder.addStatement("$N[$L] = $T.class",
                                    arrayName, iteration, classElementValue);
                        } else {
                            //array element primitive value handle
                            Class<?> elementValueClass = listElementValue.getClass();
                            TypeName elementValueClassName = ClassName.get(elementValueClass);
                            String cast = "";
                            if (elementValueClassName.isBoxedPrimitive()) {
                                elementValueClassName = elementValueClassName.unbox();
                                cast = "(" + elementValueClassName.toString() + ")";
                            }
                            if (iteration == 0) {
                                arrayValuesInitBuilder.add(
                                        createArray(elementValueClassName, arrayName, listValue)
                                );
                            }
                            arrayValuesInitBuilder.addStatement("$N[$L] = $L $L",
                                    arrayName, iteration, cast, listElementValue);
                        }
                    } else {
                        //can ever happen?
                        throw new RuntimeException("unsupported type %s for annotation value"
                                .formatted(listElement.getClass().getCanonicalName()));
                    }
                    iteration++;
                }
                CodeBlock arrayCode = arrayValuesInitBuilder.build();
                propertiesInit.add(arrayCode);
                propertiesInit.addStatement("$N.put($S, $N)", mapName, property, arrayName);
            } else if (objectValue instanceof String) {
                //string handle
                String stringValue = (String) objectValue;
                propertiesInit.addStatement("$N.put($S, $S)", mapName, property, stringValue);
            } else if (objectValue instanceof VariableElement) {
                //enum handle
                VariableElement enumValue = (VariableElement) objectValue;
                EnumHolder enumHolder = new EnumHolder(enumValue);
                CodeBlock enumConstant = enumHolder.getEnumConstant();
                propertiesInit.addStatement("$N.put($S, $L)", mapName, property, enumConstant);
            } else if (objectValue instanceof TypeMirror) {
                //class handle
                TypeName classValue = ClassName.get((TypeMirror) objectValue);
                propertiesInit.addStatement("$N.put($S, $T.class)", mapName, property, classValue);
            } else if (objectValue instanceof AnnotationMirror) {
                //annotation handle
                AnnotationMirror internalAnnotationValue = (AnnotationMirror) objectValue;
                String deepPropertiesName = "_" + propertiesName;
                CodeBlock internalProps = parseAnnotationProperties(internalAnnotationValue, deepPropertiesName);
                propertiesInit.add(internalProps);
                propertiesInit.addStatement("$N.put($S, $L)", mapName, property, deepPropertiesName);
            } else {
                //primitive value handle
                Class<?> elementValueClass = objectValue.getClass();
                TypeName elementValueClassName = ClassName.get(elementValueClass);
                String cast = "";
                if (elementValueClassName.isBoxedPrimitive()) {
                    elementValueClassName = elementValueClassName.unbox();
                    cast = "(" + elementValueClassName.toString() + ")";
                }
                propertiesInit.addStatement("$N.put($S, $L$L)", mapName, property, cast, objectValue);
            }
            annotationOrder++;
        }
        propertiesInit.addStatement("$T $N = new $T($N)",
                AnnotationProperties.class, propertiesName,
                AnnotationProperties.class, mapName);
        return propertiesInit.build();
    }

    private CodeBlock createArray(Class<?> type, String name, List<?> list) {
        return initializeArray(type, type, name, list.size());
    }

    private CodeBlock createArray(TypeName type, String name, List<?> list) {
        return initializeArray(type, type, name, list.size());
    }

    @RequiredArgsConstructor
    private static class EnumHolder {

        private final VariableElement enumElement;

        public CodeBlock getEnumConstant() {
            TypeName enumClassName = getEnumClass();
            String value = enumElement.toString();
            return CodeBlock.of("$T.valueOf($S)", enumClassName, value);
        }

        public TypeName getEnumClass() {
            TypeMirror enumType = enumElement.asType();
            return ClassName.get(enumType);
        }

    }
}
