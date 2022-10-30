package com.example.ThreadPoolBulkhead.controller;

import com.example.SemaphoreBulkhead.model.Rating;
import com.example.ThreadPoolBulkhead.model.Order;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    private final ThreadPoolBulkhead threadPoolBulkhead = threadPoolBulkhead();

    @GetMapping(value = "/order")
    @Bulkhead(name = "ratingService", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<Order> saveOrder() {
        try {
            Rating r = restTemplate.getForObject("http://localhost:8081/rating", Rating.class);
            log.info("call processed finished ->" + Thread.currentThread().getName());
            return CompletableFuture.completedFuture(new Order("1", "Ahmednagar"));
        } catch (BulkheadFullException e) {
            log.error("[Rating service] Bulkhead Full {} -  : {} ", e.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    @GetMapping(value = "/order/rating")
    public Rating getOrder() {
        try {
            Supplier<Rating> supplier = () -> restTemplate.getForObject("http://localhost:8081/rating", Rating.class);

            log.info("call processed finished ->" + Thread.currentThread().getName());
            threadPoolBulkhead.executeSupplier(supplier).toCompletableFuture().get();
            return new Rating("1", "Stationary");
        } catch (BulkheadFullException | InterruptedException | ExecutionException e) {
            log.error("[ratingService] Bulkhead Full {} -  : {} ", e.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    public static ThreadPoolBulkhead threadPoolBulkhead() {
        Duration keepAliveDuration = ThreadPoolBulkheadConfig.DEFAULT_KEEP_ALIVE_DURATION;
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(4)
                .coreThreadPoolSize(4)
                .queueCapacity(2)
                .keepAliveDuration(keepAliveDuration)
                .build();
        ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
        return registry.bulkhead("ratingService");
    }
}
