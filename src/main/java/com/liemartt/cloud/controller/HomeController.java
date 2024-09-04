package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.BreadcrumbLink;
import com.liemartt.cloud.dto.CustomUserDetails;
import com.liemartt.cloud.dto.FileResponse;
import com.liemartt.cloud.dto.FolderResponse;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.DownloadFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.PathNotExistsException;
import com.liemartt.cloud.service.FileStorageService;
import com.liemartt.cloud.service.FolderStorageService;
import com.liemartt.cloud.util.MinioUtil;
import com.liemartt.cloud.util.PathUtil;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final FileStorageService fileStorageService;
    private final FolderStorageService folderStorageService;
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final MinioUtil minioUtil;
    
    
    @GetMapping
    public String getHomePage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @RequestParam(required = false, defaultValue = "") String path,
                              Model model) {
        String userPath = PathUtil.addUserPrefix(customUserDetails.getId(), path);
        
        if (!minioUtil.isPathExists(userPath)) {
            throw new PathNotExistsException();
        }
        
        logger.info("Fetching info for user with id {}", customUserDetails.getId());
        List<FolderResponse> userFolders = folderStorageService.getUserFolders(userPath);
        List<FileResponse> userFiles = fileStorageService.getUserFiles(userPath);
        List<BreadcrumbLink> breadcrumbLinks = minioUtil.getBreadcrumbLinks(path);
        
        
        model.addAttribute("path", path);
        model.addAttribute("files", userFiles);
        model.addAttribute("folders", userFolders);
        model.addAttribute("breadcrumbLinks", breadcrumbLinks);
        model.addAttribute("username", customUserDetails.getUsername());
        
        model.addAttribute("uploadFileRequest", new UploadFileRequest());
        model.addAttribute("deleteFileRequest", new DeleteFileRequest());
        model.addAttribute("renameFileRequest", new RenameFileRequest());
        model.addAttribute("downloadFileRequest", new DownloadFileRequest());
        
        model.addAttribute("uploadFolderRequest", new UploadFolderRequest());
        model.addAttribute("deleteFolderRequest", new DeleteFolderRequest());
        model.addAttribute("renameFolderRequest", new RenameFolderRequest());
        model.addAttribute("createFolderRequest", new CreateFolderRequest());
        
        return "index";
    }
    
}
