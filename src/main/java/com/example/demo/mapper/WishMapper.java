package com.example.demo.mapper;

import com.example.demo.dto.request.WishRequestDto;
import com.example.demo.dto.response.WishResponseDto;
import com.example.demo.entity.Wish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishMapper {

    @Mapping(target = "ownerUsername", source = "owner.username")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "groupTitle", source = "wishGroup.title")
    WishResponseDto toResponseDto(Wish wish);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "wishGroup", ignore = true)
    @Mapping(target = "status", ignore = true)
    Wish toEntity(WishRequestDto dto);
}




