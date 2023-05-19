package com.tongtong.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        if (requestPath.startsWith("/status") || requestPath.startsWith("/actuator/prometheus")) {
            return chain.filter(exchange);
        }

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                            ServerHttpResponse response = exchange.getResponse();
                            HttpStatusCode responseStatus = response.getStatusCode();
                            if (responseStatus == null || responseStatus.isError()) {
                                return;
                            }
                        }

                ));
    }

    @Override
    public int getOrder() {
        return 3;
    }
}