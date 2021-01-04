package net.example.server.grpc.interceptor;

import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.google.common.base.Strings.nullToEmpty;

@GRpcGlobalInterceptor
public class UserInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        String authHeader = nullToEmpty(headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)));
        if (authHeader.startsWith("Basic ")) {
            String[] tokens = decodeBasicAuth(authHeader);
            Authentication authRequest = new UsernamePasswordAuthenticationToken(tokens[0], null);
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        } else {
            Authentication authRequest = new UsernamePasswordAuthenticationToken("anonymous", null);
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        return next.startCall(call, headers);
    }

    private String[] decodeBasicAuth(String authHeader) {
        String basicAuth;
        try {
            basicAuth = new String(Base64.getDecoder().decode(authHeader.substring(6).getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        int delim = basicAuth.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        return new String[]{basicAuth.substring(0, delim), basicAuth.substring(delim + 1)};
    }
}
