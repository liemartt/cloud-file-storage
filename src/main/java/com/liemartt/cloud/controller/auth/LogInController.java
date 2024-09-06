package com.liemartt.cloud.controller.auth;


import com.liemartt.cloud.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LogInController {
    @GetMapping
    public String getLoginPage(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/login";
    }
}
