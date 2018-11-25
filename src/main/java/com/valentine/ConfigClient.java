package com.valentine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Valentine
 * @since 2018/11/25 15:52
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConfigClient {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClient.class, args);
	}
}
