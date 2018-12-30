package com.github.johanfredin.springdataextensions;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.fail;

public class PropertyValidator {

    public static void assertPropertyIsInvalid(String property, Set<? extends ConstraintViolation<?>> violations) {
        boolean errorFound = violations
                .stream()
                .anyMatch(constraintViolation -> constraintViolation.getPropertyPath().toString().equals(property));

        if (!errorFound) {
            fail("Expected validation error for '" + property + "', but no such error exists");
        }
    }

    public static Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
