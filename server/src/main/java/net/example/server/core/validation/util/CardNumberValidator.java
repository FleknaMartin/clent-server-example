package net.example.server.core.validation.util;

import net.example.server.api.validation.ValidationResult;
import net.example.server.core.validation.util.massagepattern.ValidationPatterns;

import java.text.MessageFormat;

public class CardNumberValidator {

    private CardNumberValidator() {
    }

    public static void validate(ValidationResult validationResult, String str){
        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 != 0) {
           validationResult.addValidationError(MessageFormat.format(
                   ValidationPatterns.INVALID_CARD_NUMBER.getPattern(), str));
        }
    }
}
