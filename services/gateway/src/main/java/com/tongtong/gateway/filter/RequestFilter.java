package com.tongtong.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if (request.getURI().getPath().startsWith("/status") ||
                request.getURI().getPath().startsWith("/actuator/prometheus")) {
            return chain.filter(exchange);
        }

        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    String body = new String(bytes, StandardCharsets.UTF_8);

                    Map<String, String> requestMap = new HashMap<>();
                    requestMap.put("Uri", request.getURI().getPath());
                    requestMap.put("Url", request.getURI().toString());
                    requestMap.put("Protocol", request.getHeaders().getFirst("Upgrade") != null ? "HTTP/2" : "HTTP/1.1");
                    requestMap.put("AuthHeader", request.getHeaders().getFirst("Authorization"));
                    requestMap.put("Method", request.getMethod().toString());
                    requestMap.put("QueryString", request.getURI().getQuery());
                    requestMap.put("Body", body);
                    requestMap.put("ContentType", request.getHeaders().getFirst("Content-Type"));

                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }
}