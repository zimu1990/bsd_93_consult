package com.edu.bupt.pcs.consult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



@EnableCaching
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients("com.edu.bupt.pcs.consult.feign")
@EntityScan(basePackages="cn.edu.bupt.pcsmavenjpa.entity")
public class ConsultApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultApplication.class, args);
    }
}
