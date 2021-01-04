package net.example.server.grpc.endpoint;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.grpc.PaymentServiceGrpc;
import net.example.server.grpc.adapter.GrpcPaymentAdapter;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
@Slf4j
public class PaymentServiceEndpoint extends PaymentServiceGrpc.PaymentServiceImplBase {

    @Autowired
    private GrpcPaymentAdapter adapter;

    @Override
    public void createPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        PaymentResponse response = adapter.createPayment(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<PaymentRequest> streamPayments(StreamObserver<PaymentResponse> responseObserver) {
        return new StreamObserver<PaymentRequest>() {
            @Override
            public void onNext(PaymentRequest singleRequest) {
                PaymentResponse response = adapter.createPayment(singleRequest);
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                log.error("on Error", t);
            }

            @Override
            public void onCompleted() {
                log.debug("onComplete");
            }
        };
    }
}
