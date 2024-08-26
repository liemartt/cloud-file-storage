package com.liemartt.cloud.service;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FolderService extends MinioService {
    private final MinioClient minioClient;
    
    @SneakyThrows
    public void createFolder(String path, String name) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path + name + "/")
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }
    
    @SneakyThrows
    public void uploadFolder(String path, MultipartFile[] files) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(path + fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }
        }
    }
    
    @SneakyThrows
    public void renameFolder(String path, String folderName, String newFolderName) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path + folderName)
                .recursive(true)
                .build());
        
        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();
            String newObjectName = objectName.replaceFirst(folderName, newFolderName);
            
            minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(newObjectName)
                    .source(CopySource.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build())
                    .build());
            
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        }
    }
    
    @SneakyThrows
    public void deleteFolder(String path, String folderName) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path + folderName)
                .recursive(true)
                .build());
        
        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();
            
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        }
    }
}
