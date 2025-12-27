package com.example.demo.service.impl;

import com.example.demo.dto.request.WishGroupRequestDto;
import com.example.demo.dto.response.WishGroupResponseDto;
import com.example.demo.dto.response.WishResponseDto;

import java.util.List;

public interface WishGroupService {

    WishGroupResponseDto create(WishGroupRequestDto dto);

    WishGroupResponseDto getById(Long id);

    List<WishGroupResponseDto> getMyGroups();

    void delete(Long id);
}
