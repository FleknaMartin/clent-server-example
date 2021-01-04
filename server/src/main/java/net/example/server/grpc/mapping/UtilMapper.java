package net.example.server.grpc.mapping;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import net.example.grpc.BigDecimal;
import net.example.grpc.BigInteger;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface UtilMapper {

    @Named("bigDecimal2GrpcBigDecimal")
    static BigDecimal bigDecimal2GrpcBigDecimal(java.math.BigDecimal source) {
        BigInteger.Builder intResultBuilder = BigInteger.newBuilder();
        ByteString bytes = ByteString.copyFrom(source.unscaledValue().toByteArray());
        intResultBuilder.setValue(bytes);
        BigDecimal.Builder resultBuilder = BigDecimal.newBuilder();
        resultBuilder.setIntVal(intResultBuilder);
        resultBuilder.setScale(source.scale());
        return resultBuilder.build();
    }

    @Named("grpcBigDecimal2BigDecimal")
    static java.math.BigDecimal grpcBigDecimal2BigDecimal(BigDecimal source) {
        java.math.BigInteger bigInteger = new java.math.BigInteger(source.getIntVal().getValue().toByteArray());
        return new java.math.BigDecimal(bigInteger, source.getScale());
    }

    @Named("zonedDateTime2GrpcTimestamp")
    static Timestamp zonedDateTime2GrpcTimestamp(ZonedDateTime source) {
        Timestamp.Builder builder = Timestamp.newBuilder();
        builder.setSeconds(source.toEpochSecond());
        builder.setNanos(source.getNano());
        return builder.build();
    }

    @Named("grpcTimestamp2ZonedDateTime")
    static ZonedDateTime grpcTimestamp2ZonedDateTime(Timestamp source) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(source.getSeconds(), source.getNanos()), ZoneOffset.UTC);
    }
}