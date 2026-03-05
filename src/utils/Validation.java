package utils;

import exception.ValidationException;


@FunctionalInterface
public interface Validation<String> {
    void validate(String value)
        throws ValidationException ;

}
