package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {
    private final AuthenticationService authenticationService;
    
    @GetMapping
    public String getSignUpPage(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/signup";
    }
    
    @PostMapping
    public String signUp(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        authenticationService.signUp(userDto);
        
        return "redirect:/login";
    }
}
