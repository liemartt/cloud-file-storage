package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.entity.Role;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.exception.UsernameAlreadyExistsException;
import com.liemartt.cloud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");
    
    
    @Test
    void signUpTest_UniqueUsername_SuccessSignUp() {
        UserDto userDto = new UserDto("unique_user", "unique_user");
        
        authenticationService.signUp(userDto);
        
        User user = userRepository.findByUsername(userDto.getUsername()).get();
        assertEquals(userDto.getUsername(), user.getUsername());
    }
    
    @Test
    void signUpTest_NonUniqueUsername_FailureSignUp() {
        UserDto user = new UserDto("non_unique_user", "non_unique_user");
        
        authenticationService.signUp(user);
        
        
        assertThrows(UsernameAlreadyExistsException.class, () -> authenticationService.signUp(user));
    }
}