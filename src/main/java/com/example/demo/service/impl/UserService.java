package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.enums.Role;

import java.util.List;

public interface UserService {

    UserResponseDto register(UserRequestDto dto);

    UserResponseDto getById(Long id);

    UserResponseDto getCurrentUser();

    UserResponseDto updateProfile(UserRequestDto dto);

    void changePassword(String oldPassword, String newPassword);

    void blockUser(Long userId);

    void changeRole(Long userId, Role role);

    List<UserResponseDto> getAllUsers();
}
