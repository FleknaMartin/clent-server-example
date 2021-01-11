package net.example.server.grpc.endpoint;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.grpc.PaymentServiceGrpc;
import net.example.server.grpc.adapter.GrpcPaymentAdapter;
import net.example.server.grpc.utils.SyncResponseExecutor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

@GRpcService
@Slf4j
@Secured(value = {})
public class PaymentServiceEndpoint extends PaymentServiceGrpc.PaymentServiceImplBase {

    @Autowired
    private GrpcPaymentAdapter adapter;

    @Override
    @Secured(value = {"ROLE_USER"})
    public void createPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        PaymentResponse response = adapter.createPayment(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Secured(value = {"ROLE_USER"})
    public StreamObserver<PaymentRequest> streamPayments(StreamObserver<PaymentResponse> responseObserver) {

        SyncResponseExecutor<PaymentResponse> responseExecutor = new SyncResponseExecutor<>(responseObserver);

        return new StreamObserver<PaymentRequest>() {
            @Override
            public void onNext(PaymentRequest singleRequest) {
                adapter.createPaymentAsync(singleRequest, responseExecutor);
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error", t);
            }

            @Override
            public void onCompleted() {
                log.debug("Completed");
            }
        };
    }
}
