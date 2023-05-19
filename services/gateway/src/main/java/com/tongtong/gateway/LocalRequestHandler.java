package com.tongtong.gateway;

import com.google.gson.Gson;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.ServiceAgent;
import com.tongtong.common.status.ServiceStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class LocalRequestHandler {

    @GetMapping(path = "/status", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> getServiceStatus(ServerHttpResponse response) throws IOException {
        ServiceStatus status = ServiceAgent.getServiceStatus(ServiceID.GatewaySvc);
        String statusStr = new Gson().toJson(status);
        response.setStatusCode(HttpStatus.OK);
        return Mono.just(statusStr);
    }

}