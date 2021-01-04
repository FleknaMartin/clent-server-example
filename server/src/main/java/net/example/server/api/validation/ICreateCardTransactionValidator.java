package net.example.server.api.validation;

import net.example.server.api.to.CreateCardPaymentTo;

public interface ICreateCardTransactionValidator {
    ValidationResult validate(CreateCardPaymentTo createCardTransaction);
}
