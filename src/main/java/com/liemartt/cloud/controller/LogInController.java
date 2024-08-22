package com.liemartt.cloud.controller;


import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.entity.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LogInController {
    @GetMapping
    public String getLoginPage(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/login";
    }
    
    @PostMapping
    public String getLoginPage(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        return "auth/login";
    }
}
