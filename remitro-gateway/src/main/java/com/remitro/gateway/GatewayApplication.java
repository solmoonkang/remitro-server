package com.remitro.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.remitro.gateway.config.JwtConfig;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(JwtConfig.class)
public class GatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
