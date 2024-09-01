package com.liemartt.cloud.service;

import org.springframework.beans.factory.annotation.Value;

public abstract class MinioAbstractClass {
    @Value("${minio.bucket.name}")
    protected String bucketName;
}
