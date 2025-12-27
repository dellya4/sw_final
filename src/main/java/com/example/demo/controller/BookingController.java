package com.example.demo.controller;

import com.example.demo.dto.response.BookingResponseDto;
import com.example.demo.service.impl.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/wish/{wishId}")
    public ResponseEntity<BookingResponseDto> bookWish(@PathVariable Long wishId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.bookWish(wishId));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

}
