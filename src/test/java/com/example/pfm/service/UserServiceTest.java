package com.example.pfm.service;

import com.example.pfm.dto.UserDTO;
import com.example.pfm.model.User;
import com.example.pfm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

     @Mock
     private UserRepository userRepository;

     @Mock
     private PasswordEncoder passwordEncoder;

     @InjectMocks
     private UserService userService;

     private UserDTO userDTO;

     @BeforeEach
     void setUp() {
          userDTO = new UserDTO();
          userDTO.setUsername("test@example.com");
          userDTO.setPassword("password123");
          userDTO.setFullName("Test User");
          userDTO.setPhoneNumber("1234567890");
     }

     @Test
     void registerUser_Success() {
          when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
          when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

          User savedUser = new User();
          savedUser.setId(1L);
          savedUser.setUsername(userDTO.getUsername());
          savedUser.setFullName(userDTO.getFullName());
          savedUser.setPassword("encodedPassword");

          when(userRepository.save(any(User.class))).thenReturn(savedUser);

          User result = userService.registerUser(userDTO);

          assertNotNull(result);
          assertEquals(1L, result.getId());
          assertEquals("test@example.com", result.getUsername());
     }

     @Test
     void registerUser_UsernameTaken() {
          when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

          assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));
     }
}
