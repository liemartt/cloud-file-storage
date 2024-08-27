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
import io.minio.errors.*;
import jakarta.validation.Valid;
import jdk.jfr.MetadataDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    
    @GetMapping
    public ResponseEntity<InputStreamResource> downloadFile(@ModelAttribute("downloadFileRequest") @Valid DownloadFileRequest request,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        
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
    
    @PostMapping
    public void uploadFile(@ModelAttribute("uploadFileRequest") @Valid UploadFileRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        
        try {
            fileService.uploadFile(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @DeleteMapping
    public void deleteFile(@ModelAttribute("deleteFileRequest") @Valid DeleteFileRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult));
        }
        try {
            fileService.deleteFile(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @PatchMapping
    public void renameFile(@ModelAttribute("renameFileRequest") @Valid RenameFileRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult));
        }
        try {
            fileService.renameFile(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
