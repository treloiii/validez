package validez.lib.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validez.lib.annotation.Validator;
import validez.lib.exceptions.InvalidException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validators {

    static final Map<Class<?>, Validator<?, ?>> validators = new HashMap<>();

    static {
        try {
            Class.forName("validez.lib.api.ValidatorsFiller");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, E extends Exception> Validator<T, E> forClass(Class<T> targetClass, Class<E> exception) {
        Validator<T, E> validator = (Validator<T, E>) validators.get(targetClass);
        if (validator == null) {
            throw new NullPointerException("No validator registered for class "
                    + targetClass.getCanonicalName());
        }
        return validator;
    }

    public static <T, E extends Exception> void validate(T target, Class<E> exception) throws E {
        Validator<T, E> validator = (Validator<T, E>) forClass(target.getClass(), exception);
        validator.validate(target, null, null);
    }

    public static <T> void validate(T target) throws InvalidException {
        Validator<T, InvalidException> validator = (Validator<T, InvalidException>)
                forClass(target.getClass(), InvalidException.class);
        validator.validate(target, null, null);
    }

    public static <T> void validateOnly(T target, String... includes) throws InvalidException {
        Validator<T, InvalidException> validator = (Validator<T, InvalidException>)
                forClass(target.getClass(), InvalidException.class);
        Set<String> includesSet = includes.length == 0 ? Collections.emptySet() : Set.of(includes);
        validator.validate(target, includesSet, null);
    }

    public static <T> void validateWithout(T target, String... excludes) throws InvalidException {
        Validator<T, InvalidException> validator = (Validator<T, InvalidException>)
                forClass(target.getClass(), InvalidException.class);
        Set<String> excludesSet = excludes.length == 0 ? Collections.emptySet() : Set.of(excludes);
        validator.validate(target, null, excludesSet);
    }

}
