package com.garment.production;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 生产管理服务启动类
 *
 * 核心功能：
 * - 生产工单管理（创建、审批、开工、暂停、完成、关闭）
 * - 工艺路线管理（工序定义、路线维护）
 * - 工位管理（工位注册、工人绑定、状态管理）
 * - 生产任务管理（任务拆分、分配、执行、报工）
 * - 质检管理（巡检、终检、来料检）
 * - 报工记录（产量统计、工时记录）
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.garment.production", "com.garment.common"})
@MapperScan("com.garment.production.infrastructure.persistence")
public class ProductionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductionServiceApplication.class, args);
    }
}