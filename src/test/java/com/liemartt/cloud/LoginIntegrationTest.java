package com.liemartt.cloud;

import com.liemartt.cloud.entity.Role;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    MockMvc mockMvc;
    
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");
    
    @Test
    public void login_ValidUser_SuccessfulRedirect() throws Exception {
        userRepository.save(new User("user", "$2a$12$.wAmO3zkRizX3.ybKteEO.vAqoO5dT0hdsJZKQklKKGJ.FnlNzhTW", Role.ROLE_USER));
        mockMvc.perform(
                        formLogin("/process_login").user("user").password("user"))
                        .andExpectAll(
                                status().is3xxRedirection(),
                                redirectedUrl("/"));
    }
    @Test
    public void login_InvalidUser_SuccessfulRedirect() throws Exception {
        mockMvc.perform(
                        formLogin("/process_login").user("invalid_user").password("invalid_password"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login?error"));
    }
    
}
