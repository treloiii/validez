package validez.processor.config;

import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.exceptions.InvalidException;

import javax.annotation.processing.Filer;

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

}
