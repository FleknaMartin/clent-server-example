package net.example.server.jpa.configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e){
            return Optional.of("unknown");
        }
    }
}
