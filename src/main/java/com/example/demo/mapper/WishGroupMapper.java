package com.example.demo.mapper;

import com.example.demo.dto.request.WishGroupRequestDto;
import com.example.demo.dto.response.WishGroupResponseDto;
import com.example.demo.entity.WishGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishGroupMapper {

    WishGroupResponseDto toResponseDto(WishGroup group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "wishes", ignore = true)
    @Mapping(target = "title", source = "title")
    WishGroup toEntity(WishGroupRequestDto dto);
}



