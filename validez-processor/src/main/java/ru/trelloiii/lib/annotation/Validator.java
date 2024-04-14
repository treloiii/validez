package ru.trelloiii.lib.annotation;

public interface Validator<T, E extends Exception> {

    void validate(T object) throws E;

}
