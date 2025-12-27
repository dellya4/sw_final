package com.example.demo.service;

import com.example.demo.dto.request.WishRequestDto;
import com.example.demo.dto.response.WishResponseDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.entity.WishGroup;
import com.example.demo.enums.Role;
import com.example.demo.enums.WishStatus;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.WishMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.WishGroupRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.impl.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final WishGroupRepository wishGroupRepository;
    private final CategoryRepository categoryRepository;
    private final WishMapper wishMapper;

    @Override
    public WishResponseDto create(WishRequestDto dto) {
        User user = SecurityUtil.getCurrentUser();

        Wish wish = wishMapper.toEntity(dto);
        wish.setOwner(user);
        wish.setStatus(WishStatus.DRAFT);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            wish.setCategory(category);
        }

        if (dto.getWishGroupId() != null) {
            WishGroup group = wishGroupRepository.findById(dto.getWishGroupId())
                    .orElseThrow(() -> new RuntimeException("Wish group not found"));

            if (!group.getOwner().getId().equals(user.getId())) {
                throw new AccessDeniedException("Cannot use someone else's group");
            }

            wish.setWishGroup(group);
        }

        return wishMapper.toResponseDto(wishRepository.save(wish));
    }


    @Override
    public WishResponseDto getById(Long id) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wish not found"));
        return wishMapper.toResponseDto(wish);
    }

    @Override
    public List<WishResponseDto> getAllPublished() {
        return wishRepository.findAllByStatus(WishStatus.PUBLISHED)
                .stream()
                .map(wishMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<WishResponseDto> getMyWishes() {
        User user = SecurityUtil.getCurrentUser();
        return wishRepository.findAllByOwner(user)
                .stream()
                .map(wishMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<WishResponseDto> getAllForModeration() {
        User user = SecurityUtil.getCurrentUser();

        if (user.getRole() != Role.ROLE_ADMIN
                && user.getRole() != Role.ROLE_MODERATOR) {
            throw new AccessDeniedException("No permission");
        }

        return wishRepository.findAll()
                .stream()
                .map(wishMapper::toResponseDto)
                .toList();
    }

    @Override
    public WishResponseDto update(Long id, WishRequestDto dto) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wish not found"));

        User user = SecurityUtil.getCurrentUser();

        if (!wish.getOwner().getId().equals(user.getId())
                && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("No permission to update");
        }

        wish.setTitle(dto.getTitle());
        wish.setPrice(dto.getPrice());
        wish.setDescription(dto.getDescription());

        return wishMapper.toResponseDto(wishRepository.save(wish));
    }

    @Override
    public void publish(Long id) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wish not found"));

        User user = SecurityUtil.getCurrentUser();

        if (!wish.getOwner().getId().equals(user.getId())
                && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("No permission to update");
        }

        wish.setStatus(WishStatus.PUBLISHED);
        wishRepository.save(wish);
    }

    @Override
    public void delete(Long id) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wish not found"));

        User user = SecurityUtil.getCurrentUser();

        if (!wish.getOwner().getId().equals(user.getId())
                && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("No permission");
        }

        wish.setStatus(WishStatus.DELETED);
        wishRepository.save(wish);
    }
}
