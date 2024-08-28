package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.ObjectResponseDto;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.util.PathUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService extends MinioService {
    private final MinioClient minioClient;
    private final FileService fileService;
    
    public void createFolder(CreateFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String name = request.getFolderName();
        
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(name)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }
    
    public void uploadFolder(UploadFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        MultipartFile[] files = request.getFiles();
        
        for (MultipartFile file : files) {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(path, file);
            fileService.uploadFile(uploadFileRequest);
        }
        
    }
    
    public void renameFolder(RenameFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String oldName = request.getOldName();
        String newName = request.getNewName();
        
        List<Item> items = getFiles(oldName, true);
        
        for (Item item : items) {
            String oldObjectName = item.objectName();
            String newObjectName = oldObjectName.replaceFirst(oldName, newName);
            
            RenameFileRequest renameFileRequest = new RenameFileRequest(oldObjectName, newObjectName);
            fileService.renameFile(renameFileRequest);
            
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest(oldObjectName);
            fileService.deleteFile(deleteFileRequest);
        }
    }
    
    public void deleteFolder(DeleteFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String folderName = request.getFolderName();
        
        List<Item> items = getFiles(folderName, true);
        
        for (Item item : items) {
            String objectName = item.objectName();
            
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest(objectName);
            fileService.deleteFile(deleteFileRequest);
        }
    }
    
    public List<Item> getFiles(String path, boolean recursive) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = new ArrayList<>();
        
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .recursive(recursive)
                .build());
        for (Result<Item> result : results) {
            items.add(result.get());
        }
        return items;
    }
    
    public List<ObjectResponseDto> getUserObjects(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = getFiles(path, false);
        return items.stream()
                .map(ObjectResponseDto::new)
                .filter(object -> !object.getName().isBlank())
                .toList();
    }
    
    public List<ObjectResponseDto> getUserFolders(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = getFiles(path, false);
        return items.stream()
                .map(ObjectResponseDto::new)
                .filter(object -> !object.getName().isBlank() && object.isDir())
                .toList();
    }
    
    public List<ObjectResponseDto> getUserFiles(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = getFiles(path, false);
        return items.stream()
                .map(ObjectResponseDto::new)
                .filter(object -> !object.getName().isBlank() && !object.isDir())
                .toList();
    }
    
}
