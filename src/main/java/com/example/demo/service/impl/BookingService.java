package com.example.demo.service.impl;

import com.example.demo.dto.response.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto bookWish (Long wishId);

    void cancelBooking(Long bookingId);

    List<BookingResponseDto> getMyBookings();
}
