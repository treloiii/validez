package validez.processor.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigProvider {

    private static ConfigHolder cachedValue;

    //for future usages
    public static void init(Filer filer) {
        if (cachedValue != null) {
            return;
        }
        cachedValue = new ConfigHolder(filer);
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
