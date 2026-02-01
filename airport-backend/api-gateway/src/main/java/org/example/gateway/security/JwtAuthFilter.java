package org.example.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("✅ JwtAuthFilter GlobalFilter INITIALIZED");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("🎯 JwtAuthFilter executing for path: " +
                exchange.getRequest().getURI().getPath());

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Pomijaj endpointy auth
        if (path.startsWith("/api/auth/")) {
            System.out.println("➡️ Skipping auth endpoint");
            return chain.filter(exchange);
        }

        // Sprawdź token
        String authHeader = request.getHeaders().getFirst("Authorization");
        System.out.println("🔑 Auth header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        System.out.println("✅ Token found, validating...");

        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String username = jwtUtil.getUsernameFromToken(token);
        System.out.println("👤 User authenticated: " + username);

        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Id", username)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}