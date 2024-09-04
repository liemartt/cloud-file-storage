package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.CustomUserDetails;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.FileOperationException;
import com.liemartt.cloud.service.FolderStorageService;
import com.liemartt.cloud.util.ErrorUtil;
import com.liemartt.cloud.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderStorageService folderStorageService;
    private final Logger logger = LoggerFactory.getLogger(FolderController.class);
    
    @PostMapping("/upload")
    public String uploadFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("uploadFolderRequest") @Valid UploadFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Empty arguments in folder upload request: {}", request);
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        logger.info("Uploading folder with files [{}] for user {}", request.getFolder(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        folderStorageService.uploadFolder(request);
        
        logger.info("Successfully uploaded folder for user {}", user.getId());
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/create")
    public String createFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("createFolderRequest") @Valid CreateFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Empty arguments in folder creation request: {}", request);
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        logger.info("Creating empty folder with name {} for user {}", request.getFolderName(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        String folderName = request.getFolderName();
        request.setFolderName(PathUtil.addSlashToFolder(folderName));
        
        folderStorageService.createFolder(request);
        
        logger.info("Successfully created folder {} for user {}", request.getFolderName(), user.getId());
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/delete")
    public String deleteFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("deleteFolderRequest") @Valid DeleteFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Empty arguments in delete folder request {}", request);
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        logger.info("Deleting folder {} of user {}", request.getFolderName(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        folderStorageService.deleteFolder(request);
        
        logger.info("Successfully deleted folder {} for user {}", request.getFolderName(), user.getId());
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/rename")
    public String renameFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("renameFolderRequest") @Valid RenameFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Empty rename folder request arguments {}", request);
            throw new FileOperationException(ErrorUtil.parseError(bindingResult));
        }
        
        logger.info("Renaming folder '{}'->'{}' for user {}", request.getOldName(), request.getNewName(), user.getId());
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        folderStorageService.renameFolder(request);
        logger.info("Successfully renamed folder '{}'->'{}' for user {}", request.getOldName(), request.getNewName(), user.getId());
        
        return "redirect:/?path=" + path;
    }
}
