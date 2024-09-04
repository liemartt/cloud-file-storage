package com.liemartt.cloud.controller;

import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.exception.InvalidUserSignUpRequestException;
import com.liemartt.cloud.service.AuthenticationService;
import com.liemartt.cloud.util.ErrorUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public String processSignUp(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidUserSignUpRequestException(ErrorUtil.parseError(bindingResult));
        }
        authenticationService.signUp(userDto);
        
        return "redirect:/login";
    }
}
