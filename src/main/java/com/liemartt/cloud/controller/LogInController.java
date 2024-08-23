package com.liemartt.cloud.controller;


import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.exception.InvalidUserRequestException;
import com.liemartt.cloud.util.ErrorParser;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.login.LoginException;

@Controller
@RequestMapping("/login")
public class LogInController {
    @GetMapping
    public String getLoginPage(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/login";
    }
    
    @PostMapping
    public String processLogin(@ModelAttribute("userDto") UserDto userDto) {
        
        return "auth/login";
    }
}
