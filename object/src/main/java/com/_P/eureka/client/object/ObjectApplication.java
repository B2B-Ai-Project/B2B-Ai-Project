package com._P.eureka.client.object;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ObjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObjectApplication.class, args);
	}

}
