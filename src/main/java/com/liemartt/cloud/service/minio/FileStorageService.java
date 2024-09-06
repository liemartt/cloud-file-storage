package com.liemartt.cloud.service.minio;

import com.liemartt.cloud.dto.FileResponse;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.DownloadFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.exception.FileOperationException;
import com.liemartt.cloud.util.PathUtil;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService extends MinioAbstractClass {
    private final MinioClient minioClient;
    private final MinioService minioService;
    private final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    
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
            logger.error("Error downloading file {}: {}", path + fileName, e.getMessage());
            throw new FileOperationException("Error downloading file");
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
            logger.error("Error uploading file {}: {}", path + file.getOriginalFilename(), e.getMessage());
            throw new FileOperationException("Error uploading file " + file.getOriginalFilename());
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
            logger.error("Error deleting file {}: {}", path + fileName, e.getMessage());
            throw new FileOperationException("Error deleting file");
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
            logger.error("Error renaming file '{}'->'{}': {}", path + oldName, path + newName, e.getMessage());
            throw new FileOperationException("Error renaming file");
        }
    }
    
    public List<FileResponse> getUserFiles(String path) {
        try {
            List<Item> items = minioService.getObjects(path, false);
            return items.stream()
                    .filter(item -> !item.isDir())
                    .filter(item->!PathUtil.extractObjectName(item.objectName()).equals(PathUtil.extractObjectName(path))) //TODO rewrite
                    .sorted(Comparator.comparing(Item::lastModified).reversed())
                    .map(FileResponse::new)
                    .filter(object -> !object.getName().isBlank())
                    .toList();
        } catch (Exception e) {
            logger.error("Error fetching files with path {}: {}", path, e.getMessage());
            throw new FileOperationException("Error fetching files");
        }
    }
}
