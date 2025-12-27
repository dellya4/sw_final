package com.example.demo.serviceTest;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_success() {
        UserRequestDto dto = new UserRequestDto();
        dto.setEmail("test@mail.com");
        dto.setPassword("1234");

        User user = new User();
        User saved = new User();
        saved.setId(1L);

        Mockito.when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(userMapper.toEntity(dto)).thenReturn(user);
        Mockito.when(passwordEncoder.encode("1234"))
                .thenReturn("encoded");
        Mockito.when(userRepository.save(user))
                .thenReturn(saved);
        UserResponseDto dtoResponse = new UserResponseDto();
        dtoResponse.setId(1L);
        dtoResponse.setEmail("test@mail.com");
        dtoResponse.setRole(Role.ROLE_USER);
        Mockito.when(userMapper.toResponseDto(saved))
                .thenReturn(dtoResponse);

        UserResponseDto response = userService.register(dto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
    }

    @Test
    void register_email_exists() {
        UserRequestDto dto = new UserRequestDto();
        dto.setEmail("test@mail.com");

        Mockito.when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new User()));

        Assertions.assertThrows(RuntimeException.class,
                () -> userService.register(dto));
    }

    @Test
    void getById_success() {
        User user = new User();
        user.setId(1L);

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail(null);
        responseDto.setRole(null);
        Mockito.when(userMapper.toResponseDto(user))
                .thenReturn(responseDto);

        UserResponseDto dto = userService.getById(1L);

        Assertions.assertEquals(1L, dto.getId());
    }

    @Test
    void updateProfile_success() {
        User current = new User();
        current.setId(1L);

        UserRequestDto dto = new UserRequestDto();
        dto.setUsername("newName");
        dto.setEmail("new@mail.com");

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(current);

            Mockito.when(userRepository.save(current)).thenReturn(current);
            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setId(1L);
            responseDto.setEmail("new@mail.com");
            responseDto.setRole(Role.ROLE_USER);
            Mockito.when(userMapper.toResponseDto(current))
                    .thenReturn(responseDto);

            UserResponseDto response = userService.updateProfile(dto);

            Assertions.assertEquals("new@mail.com", response.getEmail());
        }
    }

    @Test
    void changePassword_success() {
        User user = new User();
        user.setPassword("encoded");

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(passwordEncoder.matches("old", "encoded"))
                    .thenReturn(true);
            Mockito.when(passwordEncoder.encode("new"))
                    .thenReturn("newEncoded");

            userService.changePassword("old", "new");

            Mockito.verify(userRepository).save(user);
        }
    }

    @Test
    void blockUser_not_admin() {
        User user = new User();
        user.setRole(Role.ROLE_USER);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Assertions.assertThrows(AccessDeniedException.class,
                    () -> userService.blockUser(1L));
        }
    }

    @Test
    void getAllUsers_as_admin() {
        User admin = new User();
        admin.setRole(Role.ROLE_ADMIN);

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(new User(), new User()));
        Mockito.when(userMapper.toResponseDto(Mockito.any()))
                .thenReturn(new UserResponseDto());

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(admin);

            List<UserResponseDto> users = userService.getAllUsers();

            Assertions.assertEquals(2, users.size());
        }
    }

}
