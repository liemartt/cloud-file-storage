package com.liemartt.cloud.service;


import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.entity.Role;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.exception.UsernameAlreadyExistsException;
import com.liemartt.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    
    public void signUp(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), Role.ROLE_USER);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException("User with username " + userDto.getUsername() + " already exists");
        }
    }
    
}
