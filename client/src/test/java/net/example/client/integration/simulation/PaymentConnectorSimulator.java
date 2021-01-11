package net.example.client.integration.simulation;


import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.example.client.grpc.connector.IPaymentConnector;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static net.example.client.utils.TestConstants.MESSAGE_SIMULATION_EXCEPTION;
import static net.example.client.utils.TestConstants.MESSAGE_SIMULATION_FAILURE;

@Service
@Profile("simulation-PaymentConnector")
@Primary
public class PaymentConnectorSimulator implements IPaymentConnector {

    private Reply reply = Reply.SUCCESS;

    @Override
    public PaymentResponse sendRequest(PaymentRequest request) {
        switch (reply){
            case SUCCESS:
                return PaymentResponse.newBuilder()
                        .setResult(PaymentResponse.Result.SUCCESS)
                        .build();
            case FAILED:
                return PaymentResponse.newBuilder()
                        .setResult(PaymentResponse.Result.FAILURE)
                        .setMessage(MESSAGE_SIMULATION_FAILURE)
                        .build();
            case UNAVAILABLE:
                throw new StatusRuntimeException(Status.UNAVAILABLE);
            case EXCEPTION:
                throw new RuntimeException(MESSAGE_SIMULATION_EXCEPTION);

        }
        return PaymentResponse.newBuilder().setResult(PaymentResponse.Result.SUCCESS).build();
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public enum Reply{
        SUCCESS, FAILED, UNAVAILABLE, EXCEPTION;
    }
}
