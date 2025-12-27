package com.example.demo.service;

import com.example.demo.dto.response.BookingResponseDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.Role;
import com.example.demo.enums.WishStatus;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.impl.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WishRepository wishRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto bookWish(Long wishId) {
        User user = SecurityUtil.getCurrentUser();

        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new RuntimeException("Wish not found"));

        if (wish.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot book your own wish");
        }

        if (wish.getStatus() != WishStatus.PUBLISHED) {
            throw new RuntimeException("Wish is not available for booking");
        }

        Booking booking = new Booking();
        booking.setWish(wish);
        booking.setBookedBy(user);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setCreatedAt(LocalDateTime.now());

        wish.setStatus(WishStatus.BOOKED);

        return bookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    public void cancelBooking(Long bookingId) {
        User user = SecurityUtil.getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getBookedBy().getId().equals(user.getId())
                && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("No permission to cancel booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getWish().setStatus(WishStatus.PUBLISHED);

        bookingRepository.save(booking);
    }

    @Override
    public List<BookingResponseDto> getMyBookings() {
        User user = SecurityUtil.getCurrentUser();

        return bookingRepository.findAll()
                .stream()
                .filter(b -> b.getBookedBy().getId().equals(user.getId()))
                .map(bookingMapper::toResponseDto)
                .toList();
    }
}
