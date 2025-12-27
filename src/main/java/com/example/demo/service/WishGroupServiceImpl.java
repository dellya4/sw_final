package com.example.demo.service;

import com.example.demo.dto.request.WishGroupRequestDto;
import com.example.demo.dto.response.WishGroupResponseDto;
import com.example.demo.entity.User;
import com.example.demo.entity.WishGroup;
import com.example.demo.enums.Role;
import com.example.demo.mapper.WishGroupMapper;
import com.example.demo.repository.WishGroupRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.impl.WishGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class WishGroupServiceImpl implements WishGroupService {

    private final WishGroupRepository wishGroupRepository;
    private final WishGroupMapper wishGroupMapper;

    @Override
    public WishGroupResponseDto create(WishGroupRequestDto dto) {
        User user = SecurityUtil.getCurrentUser();

        WishGroup group = wishGroupMapper.toEntity(dto);
        group.setOwner(user);

        return wishGroupMapper.toResponseDto(wishGroupRepository.save(group));
    }

    @Override
    public WishGroupResponseDto getById(Long id) {
        WishGroup wishGroup = wishGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return wishGroupMapper.toResponseDto(wishGroup);
    }

    @Override
    public List<WishGroupResponseDto> getMyGroups() {
        User user = SecurityUtil.getCurrentUser();

        return wishGroupRepository.findAllByOwner(user)
                .stream()
                .map(wishGroupMapper::toResponseDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        WishGroup group = wishGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = SecurityUtil.getCurrentUser();

        if (!group.getOwner().getId().equals(user.getId())
                    && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only admin can delete");
        }

        wishGroupRepository.delete(group);
    }
}
