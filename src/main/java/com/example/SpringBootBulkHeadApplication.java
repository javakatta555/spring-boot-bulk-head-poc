package com.example;

import com.example.ThreadPoolBulkhead.model.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
public class SpringBootBulkHeadApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringBootBulkHeadApplication.class, args);
		int i=0;
		IntStream.range(i,10).parallel().forEach(t->
		{
			Order r = new RestTemplate().getForObject("http://localhost:8080/order/rating", Order.class);
		});
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
