package net.example.client.integration.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.JsonbMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.management.relation.RoleUnresolved;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class WebSocketClientBaseTest {

    @Value("${client.url}")
    private String url;

    private WebSocketStompClient stompClient;
    private TestHandler handler = new TestHandler();
    protected StompSession session;
    protected StompSession.Subscription subscription;
    protected List<PaymentStatusWsMessage> responses = new ArrayList<>();

    @BeforeEach
    public void init() throws Exception {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);

        stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new TestMessageConverter());

        session = stompClient.connect(url, handler).get(5000, TimeUnit.MILLISECONDS);
        subscription = session.subscribe("/user/queue/payments", handler);

    }

    @AfterEach
    public void destroy() {
        if (session != null)
            session.disconnect();
        if (stompClient != null)
            stompClient.stop();
        responses.clear();
    }

    class TestHandler extends StompSessionHandlerAdapter {
        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            if (payload instanceof PaymentStatusWsMessage)
                responses.add((PaymentStatusWsMessage) payload);
        }
    }

    class TestMessageConverter implements MessageConverter {
        ObjectMapper mapper = new ObjectMapper();

        @Override
        public Object fromMessage(Message<?> message, Class<?> targetClass){
            try {
                return mapper.readValue((byte[])message.getPayload(), PaymentStatusWsMessage.class);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        @Override
        public Message<?> toMessage(Object payload, MessageHeaders headers) {
            return new Message<byte[]>() {
                @Override
                public byte[] getPayload() {
                    try {
                        return mapper.writeValueAsBytes(payload);
                    } catch (JsonProcessingException e){
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public MessageHeaders getHeaders() {
                    return headers;
                }
            };
        }
    }
}
