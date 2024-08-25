package com.liemartt.cloud.config;

import io.minio.MinioClient;
import jakarta.validation.constraints.Min;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("sFAT9yhttM0JtdL3e7Nu", "H0kHxFbJCCWeQMaMFU5Yd2QfHY67ZPyVUqx98JHM")
                .build();
    }
}
