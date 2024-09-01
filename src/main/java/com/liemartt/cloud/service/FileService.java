package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.FileResponse;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.DownloadFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.exception.BadFileOperationException;
import com.liemartt.cloud.util.MinioUtil;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService extends MinioAbstractClass {
    private final MinioClient minioClient;
    public final MinioUtil minioUtil;
    
    public InputStream downloadFile(DownloadFileRequest request) {
        String path = request.getPath();
        String fileName = request.getFileName();
        
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path + fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new BadFileOperationException("Error downloading file");
        }
    }
    
    public void uploadFile(UploadFileRequest request) {
        String path = request.getPath();
        MultipartFile file = request.getFile();
        
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path + file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            throw new BadFileOperationException("Error uploading file");
        }
    }
    
    public void deleteFile(DeleteFileRequest request) {
        String path = request.getPath();
        String fileName = request.getFileName();
        
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path + fileName)
                    .build());
        } catch (Exception e) {
            throw new BadFileOperationException("Error deleting file");
        }
    }
    
    public void renameFile(RenameFileRequest request) {
        String path = request.getPath();
        String oldName = request.getOldName();
        String newName = request.getNewName();
        
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path + newName)
                    .source(CopySource.builder()
                            .bucket(bucketName)
                            .object(path + oldName)
                            .build())
                    .build());
            
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest(path, oldName);
            
            deleteFile(deleteFileRequest);
        } catch (Exception e) {
            throw new BadFileOperationException("Error renaming file");
        }
    }
    
    public List<FileResponse> getUserFiles(String path) {
        try {
            List<Item> items = minioUtil.getObjects(path, false);
            return items.stream()
                    .filter(item -> !item.isDir())
                    .sorted(Comparator.comparing(Item::lastModified).reversed())
                    .map(FileResponse::new)
                    .filter(object -> !object.getName().isBlank() && !object.isDir())
                    .toList();
        } catch (Exception e) {
            throw new BadFileOperationException("Error fetching files");
        }
    }
}
