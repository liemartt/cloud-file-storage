package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.CustomUserDetails;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.service.MinIOStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FilterOutputStream;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final MinIOStorageService minIOStorageService;
    
    
    @GetMapping
    public String getHomePage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        model.addAttribute("username", customUserDetails.getUsername());
        return "index";
    }
    
    @PostMapping
    public String uploadFile(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(required = false) String path) {
        if (path == null) {
            path = "/";
        }
        String pathToFile = "/user-" + customUserDetails.getId().toString() + "-files/" + path+ "/";
        
        String content = "This is a file content";
        MultipartFile multipartFile = new MockMultipartFile("file", "testfile.txt", "text/plain", content.getBytes());
        
        minIOStorageService.deleteFile(pathToFile+"newTestFile.txt");
        
        return "index";
    }
}
