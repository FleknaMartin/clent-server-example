package net.example.server.util;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import net.example.grpc.BigInteger;
import net.example.grpc.PaymentResponse;
import net.example.server.api.enm.CreateCardTransactionResult;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TestConstants {
    public static final Long ID_1 = new Long(1);
    public static final Long ID_2 = new Long(2);
    public static final String LOCALHOST = "localhost".intern();
    public static final String USER = "User".intern();
    public static final String PASSWORD = "password".intern();
    public static final String FIRST_NAME = "John".intern();
    public static final String ANOTHER_FIRST_NAME = "Carl".intern();
    public static final String LAST_NAME = "Doe".intern();
    public static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.now(ZoneOffset.UTC);
    public static final Timestamp TIMESTAMP = Timestamp.newBuilder()
            .setSeconds(ZONED_DATE_TIME.toEpochSecond())
            .setNanos(ZONED_DATE_TIME.getNano())
            .build();
    public static final String AMOUNT_STR_NO_FRACTION = "12345".intern();
    public static final String AMOUNT_STR = "123.45".intern();
    public static final String ANOTHER_AMOUNT_STR_NO_FRACTION = "45678".intern();
    public static final String ANOTHER_AMOUNT_STR = "456.78".intern();
    public static final BigDecimal BIG_DECIMAL = new BigDecimal(AMOUNT_STR);
    public static final BigDecimal ANOTHER_BIG_DECIMAL = new BigDecimal(ANOTHER_AMOUNT_STR);
    public static final BigInteger GRPC_BIG_INTEGER = BigInteger
            .newBuilder()
            .setValue(ByteString.copyFrom(new java.math.BigInteger(AMOUNT_STR_NO_FRACTION).toByteArray()))
            .build();
    public static final net.example.grpc.BigDecimal GRPC_BIG_DECIMAL = net.example.grpc.BigDecimal.newBuilder()
            .setIntVal(GRPC_BIG_INTEGER)
            .setScale(2)
            .build();
    public static final BigInteger ANOTHER_GRPC_BIG_INTEGER = BigInteger
            .newBuilder()
            .setValue(ByteString.copyFrom(new java.math.BigInteger(ANOTHER_AMOUNT_STR_NO_FRACTION).toByteArray()))
            .build();
    public static final net.example.grpc.BigDecimal ANOTHER_GRPC_BIG_DECIMAL = net.example.grpc.BigDecimal.newBuilder()
            .setIntVal(ANOTHER_GRPC_BIG_INTEGER)
            .setScale(2)
            .build();
    public static final String CARD_NUMBER = "4461573087662441".intern();
    public static final String ANOTHER_CARD_NUMBER = "7193007078541635".intern();
    public static final String THIRD_CARD_NUMBER = "8481106234821763".intern();
    public static final String INVALID_CARD_NUMBER = "4461573087662440".intern();
    public static final String MESSAGE = "message".intern();
    public static final CreateCardTransactionResult RESULT_ENUM_SUCCESS = CreateCardTransactionResult.SUCCESS;
    public static final PaymentResponse.Result GRPC_RESULT_ENUM_SUCCESS = PaymentResponse.Result.SUCCESS;
    public static final CreateCardTransactionResult RESULT_ENUM_FAILURE = CreateCardTransactionResult.FAILURE;
    public static final PaymentResponse.Result GRPC_RESULT_ENUM_FAILURE = PaymentResponse.Result.FAILURE;
}
