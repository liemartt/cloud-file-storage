package com.liemartt.cloud.controller;

import com.liemartt.cloud.config.security.CustomUserDetails;
import com.liemartt.cloud.dto.file.*;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.FileOperationException;
import com.liemartt.cloud.service.minio.FileStorageService;
import com.liemartt.cloud.service.minio.FolderStorageService;
import com.liemartt.cloud.service.minio.UserMemoryService;
import com.liemartt.cloud.util.ErrorUtil;
import com.liemartt.cloud.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;
    private final FolderStorageService folderStorageService;
    private final UserMemoryService userMemoryService;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@AuthenticationPrincipal CustomUserDetails user,
                                               @ModelAttribute("downloadFileRequest") @Valid DownloadFileRequest request,
                                               BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        logger.info("Downloading file '{}' of user {}", request.getFileName(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        InputStream fileInputStream = fileStorageService.downloadFile(request);
        
        logger.info("Successfully downloaded file '{}' of user {}", request.getFileName(), user.getId());
        
        byte[] content = fileInputStream.readAllBytes();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename(request.getFileName()).build());
        
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
    
    @PostMapping("/upload")
    public String uploadFile(@AuthenticationPrincipal CustomUserDetails user,
                             @ModelAttribute("uploadFileRequest") @Valid UploadFileRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        
        logger.info("Uploading file {} for user {}", request.getFile().getOriginalFilename(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        userMemoryService.checkUserMemory(pathWithUserPrefix, request.getFile());
        
        fileStorageService.uploadFile(request);
        
        logger.info("Successfully uploaded file '{}' for user {}", request.getFile()
                .getOriginalFilename(), user.getId());
        
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/upload/multiple")
    public String uploadFiles(@AuthenticationPrincipal CustomUserDetails user,
                              @ModelAttribute("uploadFolderRequest") @Valid UploadFolderRequest request,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        logger.info("Uploading files {} for user {}", request.getFolder(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        userMemoryService.checkUserMemory(pathWithUserPrefix, request.getFolder());
        
        folderStorageService.uploadFolder(request);
        
        logger.info("Successfully uploaded file '{}' for user {}", request.getFolder(), user.getId());
        
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/delete")
    public String deleteFile(@AuthenticationPrincipal CustomUserDetails user,
                             @ModelAttribute("deleteFileRequest") @Valid DeleteFileRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        logger.info("Deleting file '{}' of user {}", request.getFileName(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        fileStorageService.deleteFile(request);
        
        logger.info("Successfully deleted file '{}' of user {}", request.getFileName(), user.getId());
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/rename")
    public String renameFile(@AuthenticationPrincipal CustomUserDetails user,
                             @ModelAttribute("renameFileRequest") @Valid RenameFileRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        logger.info("Renaming file '{}' -> '{}' of user {}", request.getOldName(), request.getNewName(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        
        String oldName = request.getOldName();
        String fileExtension = oldName.substring(oldName.lastIndexOf(".") + 1);
        String newName = PathUtil.addExtensionToFile(request.getNewName(), fileExtension);
        request.setNewName(newName);
        
        fileStorageService.renameFile(request);
        
        logger.info("Successfully renamed file '{}' to '{}' of user {}", request.getOldName(), request.getNewName(), user.getId());
        
        return "redirect:/?path=" + path;
    }
}
