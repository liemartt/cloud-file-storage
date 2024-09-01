package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.FolderResponse;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.BadFolderOperationException;
import com.liemartt.cloud.util.MinioUtil;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService extends MinioAbstractClass {
    private final MinioClient minioClient;
    private final FileService fileService;
    public final MinioUtil minioUtil;
    
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
            throw new BadFolderOperationException("Error creating folder");
        }
    }
    
    public void uploadFolder(UploadFolderRequest request) {
        String path = request.getPath();
        MultipartFile[] files = request.getFolder();
        try {
            for (MultipartFile file : files) {
                UploadFileRequest uploadFileRequest = new UploadFileRequest(path, file);
                fileService.uploadFile(uploadFileRequest);
            }
        } catch (Exception e) {
            throw new BadFolderOperationException("Error uploading folder");
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
                fileService.renameFile(renameFileRequest);
                
                DeleteFileRequest deleteFileRequest = new DeleteFileRequest("", oldObjectName);
                fileService.deleteFile(deleteFileRequest);
            }
        } catch (Exception e) {
            throw new BadFolderOperationException("Error renaming folder");
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
                fileService.deleteFile(deleteFileRequest);
            }
        } catch (Exception e) {
            throw new BadFolderOperationException("Error deleting folder");
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
            throw new BadFolderOperationException("Error fetching folders");
        }
    }
    
    
}
