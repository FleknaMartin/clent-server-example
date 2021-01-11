package net.example.client.api.exception;

public class GrpcClientStubException extends RuntimeException{

    public GrpcClientStubException(Throwable cause) {
        super(cause);
    }
}
