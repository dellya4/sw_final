package com.example.demo.service.impl;

import com.example.demo.dto.request.WishRequestDto;
import com.example.demo.dto.response.WishResponseDto;

import java.util.List;

public interface WishService {

    WishResponseDto create(WishRequestDto dto);

    WishResponseDto getById(Long id);

    List<WishResponseDto> getAllPublished();

    List<WishResponseDto> getMyWishes();

    List<WishResponseDto> getAllForModeration();

    WishResponseDto update(Long id, WishRequestDto dto);

    void publish(Long id);

    void delete(Long id);
}
