package com.liemartt.cloud;

import io.minio.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories
@EnableRedisHttpSession
public class CloudFileStorageApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CloudFileStorageApplication.class, args);
    }
}
