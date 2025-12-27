package com.example.demo.serviceTest;

import com.example.demo.dto.request.CategoryRequestDto;
import com.example.demo.dto.response.CategoryResponseDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void create_success() {
        CategoryRequestDto dto = new CategoryRequestDto();
        Category category = new Category();

        Mockito.when(categoryMapper.toEntity(dto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category))
                .thenReturn(new CategoryResponseDto());

        CategoryResponseDto response = categoryService.create(dto);

        Assertions.assertNotNull(response);
    }

    @Test
    void delete_not_admin_should_fail() {
        User user = new User();
        user.setRole(Role.ROLE_USER);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Assertions.assertThrows(AccessDeniedException.class,
                    () -> categoryService.delete(1L));
        }
    }
}

