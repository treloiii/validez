package validez.lib.annotation;

import javax.annotation.Nullable;
import java.util.Set;

public interface Validator<T, E extends Exception> {

    void validate(T object, @Nullable Set<String> includes, @Nullable Set<String> excludes) throws E;

}
