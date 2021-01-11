package net.example.server.jpa.configuration;

import org.lognet.springboot.grpc.security.GrpcSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "net.example.server.jpa.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Optional<Authentication> authentication = Optional.ofNullable(GrpcSecurity.AUTHENTICATION_CONTEXT_KEY.get());
            return authentication.isPresent() ? authentication.map(Authentication::getName) : Optional.empty();
        };
    }
}
