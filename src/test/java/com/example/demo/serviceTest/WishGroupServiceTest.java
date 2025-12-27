package com.example.demo.serviceTest;

import com.example.demo.dto.request.WishGroupRequestDto;
import com.example.demo.dto.response.WishGroupResponseDto;
import com.example.demo.entity.User;
import com.example.demo.entity.WishGroup;
import com.example.demo.enums.Role;
import com.example.demo.mapper.WishGroupMapper;
import com.example.demo.repository.WishGroupRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.WishGroupServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WishGroupServiceTest {

    @InjectMocks
    private WishGroupServiceImpl wishGroupService;

    @Mock
    private WishGroupRepository wishGroupRepository;

    @Mock
    private WishGroupMapper wishGroupMapper;

    @Test
    void create_success() {
        User user = new User();

        WishGroupRequestDto dto = new WishGroupRequestDto();
        WishGroup group = new WishGroup();

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(wishGroupMapper.toEntity(dto)).thenReturn(group);
            Mockito.when(wishGroupRepository.save(group)).thenReturn(group);
            Mockito.when(wishGroupMapper.toResponseDto(group))
                    .thenReturn(new WishGroupResponseDto());

            WishGroupResponseDto response = wishGroupService.create(dto);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(user, group.getOwner());
        }
    }

    @Test
    void delete_not_owner_should_fail() {
        User owner = new User();
        owner.setId(1L);

        User user = new User();
        user.setId(2L);
        user.setRole(Role.ROLE_USER);

        WishGroup group = new WishGroup();
        group.setOwner(owner);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            Mockito.when(wishGroupRepository.findById(1L))
                    .thenReturn(Optional.of(group));

            Assertions.assertThrows(AccessDeniedException.class,
                    () -> wishGroupService.delete(1L));
        }
    }
}
