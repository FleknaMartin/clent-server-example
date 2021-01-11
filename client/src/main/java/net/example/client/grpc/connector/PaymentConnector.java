package net.example.client.grpc.connector;

import io.grpc.*;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import lombok.extern.slf4j.Slf4j;
import net.example.client.api.exception.GrpcClientStubException;
import net.example.client.grpc.utils.PaymentServiceBasicAuthClientInterceptor;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.grpc.PaymentServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PaymentConnector implements IPaymentConnector{

    @Value("${grpc.server.payment-service.host}")
    private String host;

    @Value("${grpc.server.payment-service.port}")
    private Integer port;

    @Value("${grpc.server.payment-service.certificate}")
    private Resource certificate;

    @Value("${grpc.server.payment-service.keepAliveTime}")
    private Long keepAliveTime;

    @Autowired
    private PaymentServiceBasicAuthClientInterceptor interceptor;

    private ManagedChannel channel;

    private PaymentServiceGrpc.PaymentServiceBlockingStub blockingStub;

    @PostConstruct
    private void init() {
        SslContext sslContext;
        try {
            sslContext = GrpcSslContexts.forClient()
                    .trustManager(certificate.getInputStream())
                    .build();
        }catch (IOException e){
            throw new GrpcClientStubException(e);
        }

        channel = NettyChannelBuilder.forAddress(host, port)
                .sslContext(sslContext)
                .intercept(interceptor)
                .keepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS)
                .build();

        blockingStub = PaymentServiceGrpc.newBlockingStub(channel);
    }

    public PaymentResponse sendRequest(PaymentRequest request) {
        return blockingStub.createPayment(request);
    }

    @PreDestroy
    private void shutdown(){
        channel.shutdown();
    }
}
