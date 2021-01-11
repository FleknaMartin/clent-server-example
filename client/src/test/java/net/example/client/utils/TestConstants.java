package net.example.client.utils;

import net.example.client.api.enums.CreatePaymentResult;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TestConstants {
    public static final Long ID = 1l;
    public static final String FIRST_NAME = "firstName".intern();
    public static final String LAST_NAME = "lastName".intern();
    public static final String CARD_NUMBER = "123456789".intern();
    public static final BigDecimal AMOUNT = new BigDecimal("123.45");
    public static final ZonedDateTime PAYMENT_TIME = ZonedDateTime.now();
    public static final String MESSAGE = "message".intern();
    public static final CreatePaymentResult RESULT = CreatePaymentResult.CREATED;
    public static final String USER_ID = "asdf".intern();
    public static final String MESSAGE_SIMULATION_FAILURE = "Simulation Failure".intern();
    public static final String MESSAGE_SIMULATION_EXCEPTION = "Simulation exception".intern();
}
