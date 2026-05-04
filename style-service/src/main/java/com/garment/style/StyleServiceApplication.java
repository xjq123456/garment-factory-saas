package com.garment.style;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.garment.style", "com.garment.common"})
@MapperScan("com.garment.style.infrastructure.persistence")
public class StyleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StyleServiceApplication.class, args);
    }
}