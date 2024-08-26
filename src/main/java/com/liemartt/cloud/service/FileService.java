package com.liemartt.cloud.service;

import com.liemartt.cloud.exception.BadFileException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileService extends MinioService {
    private final MinioClient minioClient;
    
    @SneakyThrows
    public void uploadFile(String pathToFile, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathToFile + file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (IOException e) {
            throw new BadFileException(e.getMessage());
        }
    }
    
    @SneakyThrows
    public void deleteFile(String filePath) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .build());
    }
    
    @SneakyThrows
    public void renameFile(String pathToFile, String name, String newName) {
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(bucketName)
                .object(pathToFile + newName)
                .source(CopySource.builder()
                        .bucket(bucketName)
                        .object(pathToFile + name)
                        .build())
                .build());
        
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(pathToFile + name)
                .build());
    }
}
