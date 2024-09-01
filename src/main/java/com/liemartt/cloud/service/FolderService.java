package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.BreadcrumbLink;
import com.liemartt.cloud.dto.FileResponse;
import com.liemartt.cloud.dto.FolderResponse;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService extends MinioService {
    private final MinioClient minioClient;
    private final FileService fileService;
    
    public void createFolder(CreateFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        String name = request.getFolderName();
        
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path + name)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }
    
    public void uploadFolder(UploadFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        MultipartFile[] files = request.getFolder();
        for (MultipartFile file : files) {
            
            UploadFileRequest uploadFileRequest = new UploadFileRequest(path, file);
            fileService.uploadFile(uploadFileRequest);
        }
        
    }
    
    public void renameFolder(RenameFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        String oldName = request.getOldName();
        String newName = request.getNewName();
        
        String oldPathToFiles = path+oldName;
        String newPathToFiles = path+newName;
        
        List<Item> items = getFiles(oldPathToFiles, true);
        for (Item item : items) {
            String oldObjectName = item.objectName();
            String newObjectName = oldObjectName.replaceFirst(oldPathToFiles, newPathToFiles);
            
            RenameFileRequest renameFileRequest = new RenameFileRequest("", oldObjectName, newObjectName);
            fileService.renameFile(renameFileRequest);
            
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest("", oldObjectName);
            fileService.deleteFile(deleteFileRequest);
        }
    }
    
    public void deleteFolder(DeleteFolderRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = request.getPath();
        String folderName = request.getFolderName();
        
        String pathToFiles = path + folderName + "/";
        
        List<Item> items = getFiles(pathToFiles, true);
        
        
        for (Item item : items) {
            String objectName = item.objectName();
            
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest("", objectName);
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
    
    public List<FileResponse> getUserObjects(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = getFiles(path, false);
        return items.stream()
                .map(FileResponse::new)
                .filter(object -> !object.getName().isBlank())
                .toList();
    }
    
    public List<FolderResponse> getUserFolders(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = getFiles(path, false);
        
        return items.stream()
                .filter(Item::isDir)
                .map(FolderResponse::new)
                .sorted(Comparator.comparing(FolderResponse::getFolderName))
                .toList();
    }
    
    public List<FileResponse> getUserFiles(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Item> items = getFiles(path, false);
        return items.stream()
                .filter(item -> !item.isDir())
                .sorted(Comparator.comparing(Item::lastModified).reversed())
                .map(FileResponse::new)
                .filter(object -> !object.getName().isBlank() && !object.isDir())
                .toList();
    }
    
    public List<BreadcrumbLink> getBreadcrumbLinks(String path) {
        List<BreadcrumbLink> links = new ArrayList<>();
        links.add(new BreadcrumbLink("Home", ""));
        
        if (path.isBlank()) {
            return links;
        } else {
            StringBuilder sb = new StringBuilder();
            String[] folders = path.split("/");
            for (String folder : folders) {
                sb.append(folder);
                links.add(new BreadcrumbLink(folder, sb.toString()));
                sb.append("/");
            }
        }
        return links;
    }
    
}
