package validez.processor.config;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//Represent file based config, currently not used, may be in future
public class ConfigHolder {

    private final Map<String, String> config;
    private final Filer filer;

    ConfigHolder(Filer filer) {
        this.filer = filer;
        this.config = parseConfig();
    }

    private Map<String, String> parseConfig() {
        try {
            Map<String, String> config = new HashMap<>();
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT,
                    "", "tmp", (Element[]) null);
            Path projectPath = Paths.get(resource.toUri())
                    .getParent()
                    .getParent()
                    .getParent();
            resource.delete();
            Path propertiesPath = projectPath
                    .resolve("src")
                    .getParent()
                    .getParent()
                    .getParent()
                    .resolve("validez.properties");
            try (BufferedReader reader = new BufferedReader(new FileReader(propertiesPath.toFile()))) {
                String configLine = reader.readLine();
                String[] configSplit = configLine.split("=");
                String propName = configSplit[0];
                String propValue = configSplit[1];
                config.put(propName, propValue);
            } catch (FileNotFoundException e) {
                return Collections.emptyMap();
            }
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<String> getValue(String key) {
        return Optional.ofNullable(config.get(key));
    }

}
