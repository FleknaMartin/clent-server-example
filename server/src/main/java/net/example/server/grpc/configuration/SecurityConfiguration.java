package net.example.server.grpc.configuration;

import org.lognet.springboot.grpc.security.EnableGrpcSecurity;
import org.lognet.springboot.grpc.security.GrpcSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableGrpcSecurity
public class SecurityConfiguration extends GrpcSecurityConfigurerAdapter {
}
