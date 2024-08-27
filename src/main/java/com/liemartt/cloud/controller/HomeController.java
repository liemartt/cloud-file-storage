package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.CustomUserDetails;
import com.liemartt.cloud.dto.file.UploadFileRequest;
import com.liemartt.cloud.service.FileService;
import com.liemartt.cloud.service.FolderService;
import com.liemartt.cloud.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final FileService fileService;
    private final FolderService folderService;
    
    
    @GetMapping
    public String getHomePage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @RequestParam(required = false, defaultValue = "") String path,
                              Model model) {
        //todo valid path
        
        model.addAttribute("username", customUserDetails.getUsername());
        model.addAttribute("userPath", PathUtil.getUserPath(customUserDetails.getId(), path));
        
        
        model.addAttribute("uploadFileRequest", new UploadFileRequest());
        return "index";
    }
    
}
