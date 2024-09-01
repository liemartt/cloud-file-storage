package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.DownloadFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.exception.BadFileException;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class FileService extends MinioService {
    private final MinioClient minioClient;
    
    public InputStream downloadFile(DownloadFileRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = request.getFileName();
        
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }
    
    public void uploadFile(UploadFileRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        MultipartFile file = request.getFile();
        
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path + file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (IOException e) {
            throw new BadFileException("Can`t save this file");
        }
    }
    
    public void deleteFile(DeleteFileRequest request)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        String fileName = request.getFileName();
        
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(path+fileName)
                .build());
    }
    
    @SneakyThrows
    public void renameFile(RenameFileRequest request) {
        String path = request.getPath();
        String oldName = request.getOldName();
        String newName = request.getNewName();

        
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(bucketName)
                .object(path+newName)
                .source(CopySource.builder()
                        .bucket(bucketName)
                        .object(path+oldName)
                        .build())
                .build());
        
        DeleteFileRequest deleteFileRequest = new DeleteFileRequest(path, oldName);
        
        deleteFile(deleteFileRequest);
    }
}
