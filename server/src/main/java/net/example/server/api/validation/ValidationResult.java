package net.example.server.api.validation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationResult {

    private final List<ValidationError> validationErrors = new ArrayList<>();

    public boolean isValid(){
        return validationErrors.isEmpty();
    }

    public void addValidationError(String message){
        validationErrors.add(new ValidationError(message));
    }
}
