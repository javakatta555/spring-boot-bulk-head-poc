package com.example.ThreadPoolBulkhead.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
/*
@Controller
@RequestMapping("/bulkhead")
public class BulkHeadMetricsController {

    @Autowired
    private BulkHeadService bulkHeadService;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/metrics")
    public ResponseEntity<Map> getMetrics() {
        Map<String, Object> response = new HashMap<>();
        bulkHeadService.getThreadPoolBulkheadMap().forEach((key, threadPoolBulkhead) -> response.put(key, threadPoolBulkhead.getMetrics()));
        return CustomResponse.ok(response);
    }

    @GetMapping("/refresh/{bulkHeadName}")
    public ResponseEntity<Map> refresh(@PathVariable String bulkHeadName) {
        RTopic topic = redissonClient.getTopic(BULKHEAD);
        topic.publish(bulkHeadName);
        return CustomResponse.ok();
    }

    @GetMapping("/refreshAll")
    public ResponseEntity<Map> refreshAll() {
        RTopic topic = redissonClient.getTopic(BULKHEAD);
        topic.publish(REFRESH_ALL);
        return CustomResponse.ok();
    }

}*/
