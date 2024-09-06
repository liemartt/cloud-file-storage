package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.FolderResponse;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.FolderOperationException;
import com.liemartt.cloud.util.FileSizeUtil;
import com.liemartt.cloud.util.MinioUtil;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderStorageService extends MinioAbstractClass {
    private final MinioClient minioClient;
    private final FileStorageService fileStorageService;
    private final MinioUtil minioUtil;
    private final Logger logger = LoggerFactory.getLogger(FolderStorageService.class);
    
    public void createFolder(CreateFolderRequest request) {
        String path = request.getPath();
        String name = request.getFolderName();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path + name)
                            .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build());
        } catch (Exception e) {
            logger.error("Error creating folder {}: {}", path + name, e.getMessage());
            throw new FolderOperationException("Error creating folder");
        }
    }
    
    public void uploadFolder(UploadFolderRequest request) {
        String path = request.getPath();
        MultipartFile[] files = request.getFolder();
        try {
            for (MultipartFile file : files) {
                UploadFileRequest uploadFileRequest = new UploadFileRequest(path, file);
                fileStorageService.uploadFile(uploadFileRequest);
            }
        } catch (Exception e) {
            logger.error("Error uploading folder {}: {}", path + Arrays.toString(files), e.getMessage());
            throw new FolderOperationException("Error uploading folder");
        }
        
    }
    
    public void renameFolder(RenameFolderRequest request) {
        String path = request.getPath();
        String oldName = request.getOldName();
        String newName = request.getNewName();
        
        String oldPathToFiles = path + oldName;
        String newPathToFiles = path + newName;
        
        try {
            List<Item> items = minioUtil.getObjects(oldPathToFiles, true);
            for (Item item : items) {
                String oldObjectName = item.objectName();
                String newObjectName = oldObjectName.replaceFirst(oldPathToFiles, newPathToFiles);
                
                RenameFileRequest renameFileRequest = new RenameFileRequest("", oldObjectName, newObjectName);
                fileStorageService.renameFile(renameFileRequest);
                
                DeleteFileRequest deleteFileRequest = new DeleteFileRequest("", oldObjectName);
                fileStorageService.deleteFile(deleteFileRequest);
            }
        } catch (Exception e) {
            logger.error("Error renaming folder '{}'->'{}': {}", path + oldName, path + newName, e.getMessage());
            throw new FolderOperationException("Error renaming folder");
        }
    }
    
    public void deleteFolder(DeleteFolderRequest request) {
        String path = request.getPath();
        String folderName = request.getFolderName();
        
        String pathToFiles = path + folderName + "/";
        
        try {
            List<Item> items = minioUtil.getObjects(pathToFiles, true);
            
            for (Item item : items) {
                String objectName = item.objectName();
                
                DeleteFileRequest deleteFileRequest = new DeleteFileRequest("", objectName);
                fileStorageService.deleteFile(deleteFileRequest);
            }
        } catch (Exception e) {
            logger.error("Error deleting folder {}: {}", pathToFiles, e.getMessage());
            throw new FolderOperationException("Error deleting folder");
        }
    }
    
    
    public List<FolderResponse> getUserFolders(String path) {
        try {
            List<Item> items = minioUtil.getObjects(path, false);
            return items.stream()
                    .filter(Item::isDir)
                    .map(FolderResponse::new)
                    .sorted(Comparator.comparing(FolderResponse::getFolderName))
                    .toList();
        } catch (Exception e) {
            logger.error("Error fetching user folders in {}", path);
            throw new FolderOperationException("Error fetching folders");
        }
    }
    
    
    
}
