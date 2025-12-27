package com.example.demo.mapper;

import com.example.demo.dto.response.BookingResponseDto;
import com.example.demo.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "wishId", source = "wish.id")
    @Mapping(target = "wishTitle", source = "wish.title")
    @Mapping(target = "bookedByUsername", source = "bookedBy.username")
    BookingResponseDto toResponseDto(Booking booking);

}
