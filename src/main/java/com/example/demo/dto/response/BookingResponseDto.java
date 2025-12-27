package com.example.demo.dto.response;

import com.example.demo.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BookingResponseDto {

    private Long id;
    private Long wishId;
    private String wishTitle;
    private String bookedByUsername;
    private BookingStatus status;
    private LocalDateTime createdAt;
}
