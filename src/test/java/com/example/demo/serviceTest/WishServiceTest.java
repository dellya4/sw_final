package com.example.demo.serviceTest;

import com.example.demo.dto.request.WishRequestDto;
import com.example.demo.dto.response.WishResponseDto;
import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.entity.WishGroup;
import com.example.demo.enums.Role;
import com.example.demo.enums.WishStatus;
import com.example.demo.mapper.WishMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.WishGroupRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.WishServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @InjectMocks
    private WishServiceImpl wishService;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private WishGroupRepository wishGroupRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WishMapper wishMapper;

    @Test
    void create_success() {
        User user = new User();
        user.setId(1L);

        WishRequestDto dto = new WishRequestDto();
        Wish wish = new Wish();
        Wish saved = new Wish();

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(wishMapper.toEntity(dto)).thenReturn(wish);
            Mockito.when(wishRepository.save(wish)).thenReturn(saved);
            Mockito.when(wishMapper.toResponseDto(saved))
                    .thenReturn(new WishResponseDto());

            WishResponseDto response = wishService.create(dto);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(WishStatus.DRAFT, wish.getStatus());
            Assertions.assertEquals(user, wish.getOwner());
        }
    }

    @Test
    void create_with_foreign_group_should_fail() {
        User user = new User();
        user.setId(1L);

        User another = new User();
        another.setId(2L);

        WishGroup group = new WishGroup();
        group.setOwner(another);

        WishRequestDto dto = new WishRequestDto();
        dto.setWishGroupId(10L);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(wishGroupRepository.findById(10L))
                    .thenReturn(Optional.of(group));
            Mockito.when(wishMapper.toEntity(dto))
                    .thenReturn(new Wish());

            Assertions.assertThrows(AccessDeniedException.class,
                    () -> wishService.create(dto));
        }
    }

    @Test
    void getById_success() {
        Wish wish = new Wish();

        Mockito.when(wishRepository.findById(1L))
                .thenReturn(Optional.of(wish));
        Mockito.when(wishMapper.toResponseDto(wish))
                .thenReturn(new WishResponseDto());

        WishResponseDto dto = wishService.getById(1L);

        Assertions.assertNotNull(dto);
    }

    @Test
    void getAllPublished_success() {
        Mockito.when(wishRepository.findAllByStatus(WishStatus.PUBLISHED))
                .thenReturn(List.of(new Wish(), new Wish()));
        Mockito.when(wishMapper.toResponseDto(Mockito.any()))
                .thenReturn(new WishResponseDto());

        List<WishResponseDto> wishes = wishService.getAllPublished();

        Assertions.assertEquals(2, wishes.size());
    }

    @Test
    void getMyWishes_success() {
        User user = new User();
        user.setId(1L);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(wishRepository.findAllByOwner(user))
                    .thenReturn(List.of(new Wish()));
            Mockito.when(wishMapper.toResponseDto(Mockito.any()))
                    .thenReturn(new WishResponseDto());

            List<WishResponseDto> wishes = wishService.getMyWishes();

            Assertions.assertEquals(1, wishes.size());
        }
    }

    @Test
    void getAllForModeration_as_admin() {
        User admin = new User();
        admin.setRole(Role.ROLE_ADMIN);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(admin);

            Mockito.when(wishRepository.findAll())
                    .thenReturn(List.of(new Wish(), new Wish()));
            Mockito.when(wishMapper.toResponseDto(Mockito.any()))
                    .thenReturn(new WishResponseDto());

            List<WishResponseDto> wishes = wishService.getAllForModeration();

            Assertions.assertEquals(2, wishes.size());
        }
    }

    @Test
    void getAllForModeration_as_user_should_fail() {
        User user = new User();
        user.setRole(Role.ROLE_USER);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Assertions.assertThrows(AccessDeniedException.class,
                    () -> wishService.getAllForModeration());
        }
    }

    @Test
    void update_as_owner_success() {
        User user = new User();
        user.setId(1L);

        Wish wish = new Wish();
        wish.setOwner(user);

        WishRequestDto dto = new WishRequestDto();

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(wishRepository.findById(1L))
                    .thenReturn(Optional.of(wish));
            Mockito.when(wishRepository.save(wish))
                    .thenReturn(wish);
            Mockito.when(wishMapper.toResponseDto(wish))
                    .thenReturn(new WishResponseDto());

            WishResponseDto response = wishService.update(1L, dto);

            Assertions.assertNotNull(response);
        }
    }

    @Test
    void delete_not_owner_not_admin_should_fail() {
        User owner = new User();
        owner.setId(1L);

        User user = new User();
        user.setId(2L);
        user.setRole(Role.ROLE_USER);

        Wish wish = new Wish();
        wish.setOwner(owner);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(wishRepository.findById(1L))
                    .thenReturn(Optional.of(wish));

            Assertions.assertThrows(AccessDeniedException.class,
                    () -> wishService.delete(1L));
        }
    }

}

