package com.liemartt.cloud.service;

import org.springframework.beans.factory.annotation.Value;

public abstract class MinioService {
    @Value("minio.bucket.name")
    protected String bucketName;
}
