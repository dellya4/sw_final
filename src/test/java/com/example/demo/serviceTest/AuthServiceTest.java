package com.example.demo.serviceTest;

import com.example.demo.dto.request.AuthRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void login_success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("encoded");
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        Mockito.when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("1234", "encoded"))
                .thenReturn(true);

        AuthRequest request = new AuthRequest("test@mail.com", "1234");

        AuthResponse response = authService.login(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getUserId());
        Assertions.assertEquals(Role.ROLE_USER, response.getUserRole());
    }

    @Test
    void login_wrong_password() {
        User user = new User();
        user.setPassword("encoded");
        user.setEnabled(true);

        Mockito.when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("bad", "encoded"))
                .thenReturn(false);

        Assertions.assertThrows(RuntimeException.class,
                () -> authService.login(new AuthRequest("test@mail.com", "bad")));
    }
}
