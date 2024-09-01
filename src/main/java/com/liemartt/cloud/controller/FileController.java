package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.CustomUserDetails;
import com.liemartt.cloud.dto.file.DeleteFileRequest;
import com.liemartt.cloud.dto.file.DownloadFileRequest;
import com.liemartt.cloud.dto.file.RenameFileRequest;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.exception.BadFileException;
import com.liemartt.cloud.service.FileService;
import com.liemartt.cloud.util.ErrorParser;
import com.liemartt.cloud.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

import org.springframework.http.HttpHeaders;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    
    
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@AuthenticationPrincipal CustomUserDetails user,
                                                            @ModelAttribute("downloadFileRequest") @Valid DownloadFileRequest request,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.getPathWithUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        try {
            InputStream fileInputStream = fileService.downloadFile(request);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + request.getFileName() + "\"");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(fileInputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @PostMapping("/upload")
    public String uploadFile(@AuthenticationPrincipal CustomUserDetails user,
                           @ModelAttribute("uploadFileRequest") @Valid UploadFileRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.getPathWithUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        
        try {
            fileService.uploadFile(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/?path="+path;
    }
    
    @PostMapping("/delete")
    public String deleteFile(@AuthenticationPrincipal CustomUserDetails user,
                           @ModelAttribute("deleteFileRequest") @Valid DeleteFileRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult));
        }
        
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.getPathWithUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        try {
            fileService.deleteFile(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/?path="+path;
    }
    
    @PostMapping("/rename")
    public String renameFile(@AuthenticationPrincipal CustomUserDetails user,
                           @ModelAttribute("renameFileRequest") @Valid RenameFileRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult));
        }
        String path = request.getPath();
        String pathWithUserPrefix = PathUtil.getPathWithUserPrefix(user.getId(), path);
        request.setPath(pathWithUserPrefix);
        try {
            fileService.renameFile(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/?path="+path;
    }
}
