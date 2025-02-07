package com.example.cart.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "orchestration", path = "/api/orchestration")
public interface OrchestrationFeignClient {

    @PostMapping("/v1/transaction/{transactionId}/{event}")
    ResponseEntity<Void> sendEvent(@PathVariable String transactionId, @PathVariable String event);

}
