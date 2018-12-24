package com.github.johanfredin.springdataextensions.web.mvc.controller.mock;

import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;

/**
 * Mock implementation of {@link BindingResult} interface used in controllers post methods
 *
 * @author johan
 */
public class MockBindingResult extends AbstractBindingResult {

    private static final long serialVersionUID = 1L;

    private boolean hasErrors;

    public MockBindingResult(String caller) {
        this(false, caller);
    }

    public MockBindingResult(boolean hasErrors, String caller) {
        super(caller);
        this.hasErrors = hasErrors;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    protected Object getActualFieldValue(String field) {
        return null;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    @Override
    public boolean hasErrors() {
        return this.hasErrors;
    }


}
