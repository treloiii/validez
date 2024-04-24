package validez.processor.config;

import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.exceptions.InvalidException;

import javax.annotation.processing.Filer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigProvider {

    private static ConfigHolder cachedValue;

    public static void init(Filer filer) {
        if (cachedValue != null) {
            return;
        }
        cachedValue = new ConfigHolder(filer);
    }

    public static ClassName getExceptionClass() {
        if (cachedValue == null) {
            throw new RuntimeException("config is not initialized");
        }
        return cachedValue.getValue("validator.exception")
                .map(ClassName::bestGuess)
                .orElse(ClassName.get(InvalidException.class));
    }

    public static String getProcessorVersion() {
        try (InputStream inputStream = ConfigProvider.class
                .getClassLoader()
                .getResourceAsStream("META-INF/gradle.properties")) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String propertiesLine = reader.readLine();
                    return propertiesLine.split("=")[1].trim();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("cannot load processor version", e);
        }
        throw new RuntimeException("cannot load processor version");
    }

}
