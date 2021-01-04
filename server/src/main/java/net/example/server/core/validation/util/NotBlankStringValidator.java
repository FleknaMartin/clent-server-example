package net.example.server.core.validation.util;

import net.example.server.api.validation.ValidationResult;
import net.example.server.core.validation.util.massagepattern.ValidationPatterns;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class NotBlankStringValidator {

    private NotBlankStringValidator() {
    }

    public static void validate(ValidationResult validationResult, String str, String strName){
        if(StringUtils.isBlank(str)) {
            validationResult.addValidationError(MessageFormat.format(ValidationPatterns.NOT_BLANK.getPattern(), strName));
        }
    }
}
