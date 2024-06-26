package validez.processor.utils;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

@RequiredArgsConstructor
public class ClassWriter {

    private final ProcessingEnvironment processingEnvironment;

    public boolean hasPackage(TypeElement element) {
        Elements elements = processingEnvironment.getElementUtils();
        PackageElement packageElement = elements.getPackageOf(element);
        String packageName = packageElement.getQualifiedName().toString();
        return !packageName.isBlank();
    }

    /**
     * Write generated class to package of original class
     */
    public JavaFile writeClass(TypeSpec generatedSpec, TypeElement originalElement) {
        Elements elements = processingEnvironment.getElementUtils();
        PackageElement packageElement = elements.getPackageOf(originalElement);
        String packageName = packageElement.getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, generatedSpec)
                .build();
        Filer filer = processingEnvironment.getFiler();
        String canonicalName =
                javaFile.packageName + "." + generatedSpec.name;
        try {
            JavaFileObject sourceFile = filer.createSourceFile(canonicalName);
            try (Writer writer = new BufferedWriter(sourceFile.openWriter())) {
                javaFile.writeTo(writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing source code of %s".formatted(generatedSpec.name), e);
        }
        return javaFile;
    }

}
