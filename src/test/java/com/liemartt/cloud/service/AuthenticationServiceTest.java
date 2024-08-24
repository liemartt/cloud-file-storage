package com.liemartt.cloud.service;

import com.liemartt.cloud.entity.Role;
import com.liemartt.cloud.entity.User;
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


@Testcontainers
@SpringBootTest
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
        userRepository.save(new User("artem", "qwe", Role.ROLE_USER));
        System.out.println(userRepository.findAll());
        assertEquals(1, userRepository.findAll().size());
    }
}