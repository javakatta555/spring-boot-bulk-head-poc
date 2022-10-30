package com.example.SemaphoreBulkhead.controller;


import com.example.SemaphoreBulkhead.model.Product;
import com.example.SemaphoreBulkhead.model.Rating;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.function.Supplier;


@RestController
@Slf4j
public class ProductController {

    @Autowired
    private RestTemplate restTemplate;

    private io.github.resilience4j.bulkhead.Bulkhead bulkhead = bulkhead();

    @GetMapping(value = "/health-check")
    public String checkHealth(){
        return "I am Healthy";
    }

    @GetMapping(value = "/product")
    @Bulkhead(name="ratingService",type = Bulkhead.Type.SEMAPHORE)
    public Product saveProduct() {
        try {
            Rating r = restTemplate.getForObject("http://localhost:8081/rating", Rating.class);
            log.info("call processed finished ->" + Thread.currentThread().getName());
            //return CompletableFuture.completedFuture(new Product("1","Stationary","Pencil"));
            return new Product("1", "Stationary", "Pencil");
        } catch (BulkheadFullException e) {
            log.error("[Rating service] Bulkhead Full {} -  : {} ", e.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    @GetMapping(value = "/product/rating")
    public Rating getProduct(){
        try {
            Supplier<Rating> supplier = () -> restTemplate.getForObject("http://localhost:8081/rating", Rating.class);
            bulkhead.executeSupplier(supplier);
            log.info("call processed finished ->" + Thread.currentThread().getName());
            return new Rating("1", "Stationary");
        } catch (BulkheadFullException e) {
            log.error("[ratingService] Bulkhead Full {} -  : {} ", e.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    public io.github.resilience4j.bulkhead.Bulkhead bulkhead(){
        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
                .maxConcurrentCalls(10)
                .maxWaitDuration(Duration.ofMillis(3000))
                .build();
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.of(bulkheadConfig);
        io.github.resilience4j.bulkhead.Bulkhead bulkhead= bulkheadRegistry.bulkhead("ratingService");
        return bulkhead;
    }

}
