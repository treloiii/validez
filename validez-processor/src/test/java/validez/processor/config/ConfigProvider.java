package validez.processor.config;

import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.exceptions.InvalidException;

import javax.annotation.processing.Filer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigProvider {

    public static final String VALIDATOR_EXCEPTION = "validator.exception";

    private static ConfigHolder cachedValue;
    private static final Map<String, String> overrides = new HashMap<>();

    public static void init(Filer filer) {
        if (cachedValue != null) {
            return;
        }
        cachedValue = new ConfigHolder(filer);
    }

    public static void override(String property, String value) {
        overrides.put(property, value);
    }

    public static void clearOverride(String property) {
        overrides.remove(property);
    }

    public static ClassName getExceptionClass() {
        return Optional.ofNullable(overrides.get(VALIDATOR_EXCEPTION))
                .or(() -> cachedValue.getValue(VALIDATOR_EXCEPTION))
                .map(ClassName::bestGuess)
                .orElse(ClassName.get(InvalidException.class));
    }

    public static ClassName getGlobalException() {
        return cachedValue.getValue(VALIDATOR_EXCEPTION)
                .map(ClassName::bestGuess)
                .orElse(ClassName.get(InvalidException.class));
    }

    public static String getProcessorVersion() {
        return "test";
    }

}
