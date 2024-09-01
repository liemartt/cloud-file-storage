package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.CustomUserDetails;
import com.liemartt.cloud.dto.folder.CreateFolderRequest;
import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.FileOperationException;
import com.liemartt.cloud.service.FolderStorageService;
import com.liemartt.cloud.util.ErrorParser;
import com.liemartt.cloud.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderStorageService folderStorageService;

//    @GetMapping
//    public ResponseEntity<InputStreamResource> downloadFolder(@ModelAttribute("downloadFileRequest") @Valid DownloadFileRequest request,
//                                                            BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new BadFileException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
//        }
//
//        try {
//            InputStream fileInputStream = fileService.downloadFile(request);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + request.getFileName() + "\"");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(new InputStreamResource(fileInputStream));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    
    @PostMapping("/upload")
    public String uploadFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("uploadFolderRequest") @Valid UploadFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        folderStorageService.uploadFolder(request);
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/create")
    public String createFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("createFolderRequest") @Valid CreateFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        String folderName = request.getFolderName();
        request.setFolderName(PathUtil.addSlashToFolder(folderName));
        
        folderStorageService.createFolder(request);
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/delete")
    public String deleteFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("deleteFolderRequest") @Valid DeleteFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorParser.parseError(bindingResult));
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        folderStorageService.deleteFolder(request);
        
        return "redirect:/?path=" + path;
    }
    
    @PostMapping("/rename")
    public String renameFolder(@AuthenticationPrincipal CustomUserDetails user,
                               @ModelAttribute("renameFolderRequest") @Valid RenameFolderRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FileOperationException(ErrorParser.parseError(bindingResult));
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.addUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        folderStorageService.renameFolder(request);
        
        return "redirect:/?path=" + path;
    }
}
