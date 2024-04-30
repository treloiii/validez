package validez.processor.config;

import javax.annotation.processing.Filer;
import java.util.Map;
import java.util.Optional;

public class ConfigHolder {

    public static Map<String, String> config;

    ConfigHolder(Filer filer) {
    }

    public Optional<String> getValue(String key) {
        return Optional.ofNullable(config.get(key));
    }

}
