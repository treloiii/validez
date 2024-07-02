package validez.processor.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigProvider {

    private static ConfigHolder cachedValue;
    private static final Map<String, String> overrides = new HashMap<>();

    public static void init(Filer filer) {
        if (cachedValue != null) {
            return;
        }
        cachedValue = new ConfigHolder(filer);
    }

    public static String getProcessorVersion() {
        return "test";
    }

}
