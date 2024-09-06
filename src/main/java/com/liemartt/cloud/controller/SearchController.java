package com.liemartt.cloud.controller;

import com.liemartt.cloud.config.security.CustomUserDetails;
import com.liemartt.cloud.dto.SearchResponse;
import com.liemartt.cloud.service.minio.SearchStorageService;
import com.liemartt.cloud.util.PathUtil;
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
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchStorageService searchService;
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    
    @GetMapping
    public String search(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @RequestParam String query, Model model) {
        
        String userPrefix = PathUtil.addUserPrefix(userDetails.getId(), "");
        
        logger.info("Search objects for user {}, query: {}", userDetails.getId(), query);
        
        List<SearchResponse> objects = searchService.findObjects(userPrefix, query);
        
        logger.info("Successfully found objects {}", objects);
        
        model.addAttribute("objects", objects);
        model.addAttribute("query", query);
        model.addAttribute("username", userDetails.getUsername());
        return "search";
    }
}
