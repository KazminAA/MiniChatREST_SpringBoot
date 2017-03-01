package com.minichat.helpers;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationError() {
    }

    public void addFieldError(String field, String message) {
        fieldErrors.add(new FieldError(field, message));
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
