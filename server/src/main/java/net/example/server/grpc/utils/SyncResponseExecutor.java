package net.example.server.grpc.utils;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SyncResponseExecutor<T> {
    private final ExecutorService responseExecutor = Executors.newSingleThreadExecutor();

    private StreamObserver<T> responseObserver;

    public SyncResponseExecutor(StreamObserver<T> responseObserver) {
        this.responseObserver = responseObserver;
    }

    public void sendResponse(T response) {
        Runnable onNext = () -> {
            log.info("Sending response: " +response.toString());
            responseObserver.onNext(response);
        };
        responseExecutor.execute(onNext);
    }
}
