package com.vinyl.service;

public interface Validator<T> {

	void validate(T obj);
}
