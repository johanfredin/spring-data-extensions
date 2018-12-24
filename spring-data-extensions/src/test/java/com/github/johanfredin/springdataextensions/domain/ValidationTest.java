package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@RunWith(JUnit4.class)
public abstract class ValidationTest<E extends AbstractEntity> {

    protected E entity;

    protected Validator validator = TestFixture.getValidator();

    protected Set<ConstraintViolation<E>> getViolations() {
        return this.validator.validate(this.entity);
    }

    @Before
    public abstract void init();

    @Test
    public abstract void testNotEmptyFields();

    @Test
    public abstract void testNotExceedLengthFields();

}
