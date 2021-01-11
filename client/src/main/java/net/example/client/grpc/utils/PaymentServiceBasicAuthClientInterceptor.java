package net.example.client.grpc.utils;

import io.grpc.*;
import org.lognet.springboot.grpc.security.AuthHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.Base64;

@Service
public class PaymentServiceBasicAuthClientInterceptor implements ClientInterceptor {

    @Value("${grpc.server.payment-service.user}")
    private String user;

    @Value("${grpc.server.payment-service.password}")
    private String password;

    private AuthHeader basicAuthHeader;

    @PostConstruct
    private void init(){
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());

        basicAuthHeader = AuthHeader.builder()
                .basic()
                .tokenSupplier(ByteBuffer.wrap(encodedAuth)::duplicate)
                .build();
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel next) {
        return new ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT>(next.newCall(methodDescriptor, callOptions)) {
            @Override
            protected void checkedStart(Listener<RespT> responseListener, Metadata headers) throws Exception {
                delegate().start(responseListener, basicAuthHeader.attach(headers));
            }
        };
    }
}

