package com.liemartt.cloud;

import io.minio.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class CloudFileStorageApplication {
    
        public static void main(String[] args) {
        SpringApplication.run(CloudFileStorageApplication.class, args);
    }
//    @SneakyThrows
//    public static void main(String[] args) {
//        try (MinioClient minioClient = MinioClient
//                .builder()
//                .endpoint("http://localhost:9000")
//                .credentials("sFAT9yhttM0JtdL3e7Nu", "H0kHxFbJCCWeQMaMFU5Yd2QfHY67ZPyVUqx98JHM")
//                .build();) {
//            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket("test-bucket").build())) {
//                System.out.println("bucket 'test-bucket' exists");
//                try {
////                    minioClient.uploadObject(
////                            UploadObjectArgs
////                                    .builder()
////                                    .bucket("test-bucket")
////                                    .object("/user-666-files/hello-new.txt")
////                                    .filename("D:\\JavaLearning\\cloud-file-storage\\docker-compose.yaml")
////                                    .build());
//                    minioClient.uploadObject(
//                            UploadObjectArgs
//                                    .builder()
//                                    .bucket("test-bucket")
//                                    .object("/user-666-files/new-folder")
//                                    .build());
//
//
//                    minioClient.copyObject(CopyObjectArgs.builder()
//                            .bucket("test-bucket")
//                            .object("/user-666-files/new-folder")
//                            .source(CopySource.builder()
//                                    .bucket("test-bucket")
//                                    .object("/user-666-files")
//                                    .build())
//                            .build());
//                    System.out.println("folder successfully copied");
//
////                    minioClient.removeObject(RemoveObjectArgs.builder()
////                            .bucket("test-bucket")
////                            .object("/user-666-files/hello-new.txt")
////                            .build());
//
//                    System.out.println("file successfully renamed");
//                } catch (Exception e) {
//                    e.printStackTrace();
////                    System.out.println("Error: "+ e.getMessage());
//                }
//
//            } else {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket("test-bucket").build());
//                System.out.println(minioClient.bucketExists(BucketExistsArgs.builder().bucket("test-bucket").build()));
//            }
//        }
//    }
}
