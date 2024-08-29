package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.folder.DeleteFolderRequest;
import com.liemartt.cloud.dto.folder.RenameFolderRequest;
import com.liemartt.cloud.dto.folder.UploadFolderRequest;
import com.liemartt.cloud.exception.BadFileException;
import com.liemartt.cloud.service.FolderService;
import com.liemartt.cloud.util.ErrorParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

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
    
    @PostMapping
    public void uploadFolder(@ModelAttribute("uploadFolderRequest") @Valid UploadFolderRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult)); //todo invalid request exception
        }
        System.out.println(Arrays.toString(request.getFolder()));
        
        try {
            folderService.uploadFolder(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @DeleteMapping
    public void deleteFolder(@ModelAttribute("deleteFolderRequest") @Valid DeleteFolderRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult));
        }
        try {
            folderService.deleteFolder(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @PatchMapping
    public void renameFolder(@ModelAttribute("renameFolderRequest") @Valid RenameFolderRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadFileException(ErrorParser.parseError(bindingResult));
        }
        try {
            folderService.renameFolder(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
