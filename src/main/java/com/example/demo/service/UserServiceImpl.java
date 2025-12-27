package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.impl.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponseDto register(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        User user = SecurityUtil.getCurrentUser();
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateProfile(UserRequestDto dto) {
        User user = SecurityUtil.getCurrentUser();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        User user = SecurityUtil.getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Wrong old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void blockUser(Long userId) {
        User admin = SecurityUtil.getCurrentUser();
        if (admin.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only admin can block users");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
    }

    @Override
    public void changeRole(Long userId, Role role) {
        User admin = SecurityUtil.getCurrentUser();

        if (admin.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only admin can change user roles");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (role == Role.ROLE_ADMIN) {
            throw new RuntimeException("Cannot assign ADMIN role");
        }

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        Role role = SecurityUtil.getCurrentUser().getRole();

        if (role != Role.ROLE_ADMIN && role != Role.ROLE_MODERATOR) {
            throw new AccessDeniedException("Only admin or moderator can view all users");
        }

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }
}
