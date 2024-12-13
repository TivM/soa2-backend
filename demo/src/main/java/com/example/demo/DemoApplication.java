package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.example.demo.repository")  // Пакет с репозиториями
@EntityScan(basePackages = "org.library.entity")  // Пакет с сущностями
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		// Пример для проверки правильности настройки SSL
		System.out.println("SSL Settings:");
		System.out.println("KeyStore: " + System.getProperty("server.ssl.key-store"));
		System.out.println("TrustStore: " + System.getProperty("javax.net.ssl.trustStore"));
	}

}
