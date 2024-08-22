package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    
    @GetMapping
    public String getSignUpPage(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/signup";
    }
    
    @PostMapping
    public String signUp(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        
        return "auth/signup";
    }
}
