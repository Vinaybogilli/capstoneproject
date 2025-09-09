package com.example.surya.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.surya.filter.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth service - public
                .route("userauth-service-route", r -> r.path("/api/auth/**")
                        .uri("lb://USER-AUTHENTICATION-SERVICE"))

                // Customer service - secured
                .route("customer-service-route", r -> r.path("/api/customer/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://CustomerService"))

                // Account service - secured
                .route("account-service-route", r -> r.path("/api/bank/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://AccountService"))

                // Payment service - secured
                .route("payment-service-route", r -> r.path("/payments/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://PaymentService"))

                // Audit service - role-based
                .route("audit-service-route", r -> r.path("/api/audit/**")
                        .filters(f -> {
                            JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
                            config.setRequiredRole("ADMIN"); // or USER if needed
                            return f.filter(jwtAuthenticationFilter.apply(config));
                        })
                        .uri("lb://AUDIT-SERVICE"))

                // Admin service - only ADMIN
                .route("admin-service-route", r -> r.path("/api/admin/**")
                        .filters(f -> {
                            JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
                            config.setRequiredRole("ADMIN");
                            return f.filter(jwtAuthenticationFilter.apply(config));
                        })
                        .uri("lb://AdminService"))

                // Notification service - secured
                .route("notification-service-route", r -> r.path("/api/notification/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://NOTIFICATION-SERVICE"))

                .build();
    }
}
