package com.vinyl.service.validation;

public interface Validator<T> {

    void validate(T obj);
}
