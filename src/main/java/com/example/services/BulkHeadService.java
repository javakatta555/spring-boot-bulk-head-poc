package com.example.services;



import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;



/*
@Slf4j
@Configuration
public class BulkHeadService {

    @Getter
    private Map<String, ThreadPoolBulkhead> threadPoolBulkheadMap = new HashMap<>();

    @Autowired
    private MongoConfig mongoConfig;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CommonNotificationServiceFactory commonNotificationServiceFactory;

    @Value("${env}")
    private String env;

    @Bean(name = "threadPoolBulkheadMap")
    public Map<String, ThreadPoolBulkhead> populateThreadPoolBulkheadMap() {
        redisBulkheadEventListener(); //initializing listener
        return refreshThreadPoolBulkheadMap(); //initializing threadPoolBulkheadMap
    }

    public Map<String, ThreadPoolBulkhead> refreshThreadPoolBulkheadMap() {
        Map<String, ThreadPoolBulkhead> threadPoolBulkheadMap = new HashMap<>();
        MongoOperations mongoTemplate = mongoConfig.getMongoTemplate(MINTPRO_DB);
        List<BulkHeadConfig> bulkHeadConfigList = mongoTemplate.findAll(BulkHeadConfig.class);
        bulkHeadConfigList.forEach(bulkHeadConfig -> {
            if (bulkHeadConfig.isActive())
                threadPoolBulkheadMap.put(bulkHeadConfig.getId(), createThreadPoolBulkhead(bulkHeadConfig));
        });
        this.threadPoolBulkheadMap = threadPoolBulkheadMap;
        return threadPoolBulkheadMap;
    }

    private void redisBulkheadEventListener() {
        RTopic topic = redissonClient.getTopic(BULKHEAD);
        topic.addListener(String.class, (channel, message) -> {
            log.info("Bulkhead config update listener - " + message);
            if (REFRESH_ALL.equalsIgnoreCase(message))
                refreshThreadPoolBulkheadMap();
            else
                refresh(message);
        });
    }

    private ThreadPoolBulkhead createThreadPoolBulkhead(BulkHeadConfig bulkHeadConfig) {
        Duration keepAliveDuration = ThreadPoolBulkheadConfig.DEFAULT_KEEP_ALIVE_DURATION;
        if (bulkHeadConfig.getKeepAliveDuration() > 0)
            keepAliveDuration = Duration.ofMillis(bulkHeadConfig.getKeepAliveDuration());
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(bulkHeadConfig.getMaxThreadPool())
                .queueCapacity(bulkHeadConfig.getQueueCapacity())
                .keepAliveDuration(keepAliveDuration)
                .build();
        ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);
        return registry.bulkhead(bulkHeadConfig.getId());
    }

    public void refresh(String bulkHeadName) {
        MongoTemplate mongoTemplate = mongoConfig.getMongoTemplate(MINTPRO_DB);
        BulkHeadConfig bulkHeadConfig = mongoTemplate.findById(bulkHeadName, BulkHeadConfig.class);
        if (Objects.isNull(bulkHeadConfig))
            return;
        if (!bulkHeadConfig.isActive()) {
            this.threadPoolBulkheadMap.remove(bulkHeadName);
            return;
        }
        ThreadPoolBulkhead threadPoolBulkhead = createThreadPoolBulkhead(bulkHeadConfig);
        this.threadPoolBulkheadMap.put(bulkHeadConfig.getId(), threadPoolBulkhead);
    }

    public void sendNotification(String message) {
        RLock lock = redissonClient.getLock(message);
        if (lock.isLocked())
            return;
        lock.lock(60, MINUTES);
        Map<String, Object> data = new HashMap<>();
        final String subject = String.format("[%s] Alert!! Issue in mintpro-be service", env);
        data.put("message", "Few API's in mintpro-be are taking too much time to respond. Please check.");
        data.put("errorMsg", message);
        data.put("bulkheadList", getBulkheadList());
        MintproFailureAlertEmail mintproFailureAlertEmail = new MintproFailureAlertEmail(commonNotificationServiceFactory, data, subject);
        mintproFailureAlertEmail.send();
    }

    private List<Map<String, Object>> getBulkheadList() {
        List<Map<String, Object>> bulkheadList = new ArrayList<>();
        this.threadPoolBulkheadMap.forEach((key, value) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", key);
            map.put("coreThreadPoolSize", value.getMetrics().getCoreThreadPoolSize());
            map.put("maximumThreadPoolSize", value.getMetrics().getMaximumThreadPoolSize());
            map.put("threadPoolSize", value.getMetrics().getThreadPoolSize());
            map.put("queueCapacity", value.getMetrics().getQueueCapacity());
            map.put("queueDepth", value.getMetrics().getQueueDepth());
            map.put("remainingQueueCapacity", value.getMetrics().getRemainingQueueCapacity());
            bulkheadList.add(map);
        });
        return bulkheadList;
    }

}*/
