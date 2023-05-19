package com.tongtong.gateway.filter;

import com.tongtong.common.entity.UserAuth;
import com.tongtong.common.security.JJwtUtility;
import com.tongtong.common.security.JwtUtility;
import com.tongtong.common.security.Secured;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Secured
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    public UserAuth getUserCred(String authHeader) {
        if(authHeader==null || !authHeader.startsWith("Bearer")) {
            return null;
        }

        String authToken = authHeader.substring("Bearer".length()).trim();

        JwtUtility jwtUtility = new JJwtUtility();
        UserAuth userAuth = jwtUtility.parseToken(authToken);

        return userAuth;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String requestPath = request.getPath().value();

        if (requestPath.startsWith("/auth/token") ||
                requestPath.startsWith("/status") ||
                requestPath.startsWith("/admin/status") ||
                requestPath.startsWith("/actuator/prometheus") ||
                HttpMethod.OPTIONS.equals(request.getMethod())) {
            return chain.filter(exchange); // Proceed with the next filter in the chain
        }

        HttpHeaders headers = request.getHeaders();
        String authHeader = headers.getFirst("Authorization");

        if (authHeader == null) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain");
            response.getHeaders().add(HttpHeaders.CACHE_CONTROL, "no-store");
            response.getHeaders().add(HttpHeaders.PRAGMA, "no-cache");
            response.writeWith(Mono.just(response.bufferFactory().wrap("No Authorization header present in the request".getBytes())));
            return Mono.empty();
        }

        UserAuth userAuth = getUserCred(authHeader);
        if (userAuth == null) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain");
            response.getHeaders().add(HttpHeaders.CACHE_CONTROL, "no-store");
            response.getHeaders().add(HttpHeaders.PRAGMA, "no-cache");
            response.writeWith(Mono.just(response.bufferFactory().wrap("Unable to authenticate user token".getBytes())));
            return Mono.empty();
        }

        exchange.getAttributes().put("SecurityContext", userAuth);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}