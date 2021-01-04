package net.example.server.core.validation.util;

import net.example.server.api.validation.ValidationResult;
import net.example.server.core.validation.util.massagepattern.ValidationPatterns;

import java.text.MessageFormat;

public class NotNullValidator {

    private NotNullValidator() {
    }

    public static void validate(ValidationResult validationResult, Object object, String objectName){
        if(object == null) {
            validationResult.addValidationError(MessageFormat.format(ValidationPatterns.NOT_NULL.getPattern(), objectName));
        }
    }

    public static void validate(ValidationResult validationResult, Object object, Class objectClass){
        if(objectClass != null){
            validate(validationResult, object, objectClass.getName());
        } else {
            validate(validationResult, object, "null");
        }

    }
}
