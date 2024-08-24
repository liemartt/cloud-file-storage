package com.liemartt.cloud.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    @NotEmpty
    @Size(min=3, max = 30, message = "Username should be between 3 and 30 symbols")
    
    private String username;
    @NotEmpty
    @Size(min=3, max = 30, message = "Password should be between 3 and 30 symbols")
    private String password;
}
