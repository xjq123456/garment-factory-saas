package com.garment.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 库存服务启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.garment.inventory", "com.garment.common"})
@MapperScan("com.garment.inventory.infrastructure.persistence")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}