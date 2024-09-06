package com.liemartt.cloud.service.minio;

import org.springframework.beans.factory.annotation.Value;

public abstract class MinioAbstractClass {
    @Value("${minio.bucket.name}")
    protected String bucketName;
}
