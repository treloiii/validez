package validez.lib.api.defined;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldUtils {

    public static boolean needValidate(String field, @Nullable Set<String> includes, @Nullable Set<String> excludes) {
        if (includes == null && excludes == null) {
            return true;
        }
        if (includes != null && includes.isEmpty() && excludes != null && excludes.isEmpty()) {
            return true;
        }
        if (includes != null && excludes != null && includes.contains(field) && excludes.contains(field)) {
            throw new IllegalArgumentException(("cannot include and exclude field" +
                    " %s from validation at the same time").formatted(field));
        }
        if (includes != null) {
            return includes.contains(field);
        }
        return !excludes.contains(field);
    }

}
