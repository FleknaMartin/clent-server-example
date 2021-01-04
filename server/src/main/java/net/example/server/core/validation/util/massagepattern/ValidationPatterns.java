package net.example.server.core.validation.util.massagepattern;

public enum ValidationPatterns {
    NOT_BLANK("{0} must not be blank"),
    NOT_NULL("{0} must not be null"),
    INVALID_CARD_NUMBER("{0} is not valid card number");

    private String pattern;

    ValidationPatterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern(){
        return pattern;
    }
}
