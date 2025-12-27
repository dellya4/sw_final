package com.example.demo.serviceTest;

import com.example.demo.dto.response.BookingResponseDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.WishStatus;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.service.BookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Test
    void bookWish_success() {
        User owner = new User();
        owner.setId(1L);

        User booker = new User();
        booker.setId(2L);

        Wish wish = new Wish();
        wish.setOwner(owner);
        wish.setStatus(WishStatus.PUBLISHED);

        Booking booking = new Booking();

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(booker);

            Mockito.when(wishRepository.findById(10L))
                    .thenReturn(Optional.of(wish));
            Mockito.when(bookingRepository.save(Mockito.any()))
                    .thenReturn(booking);
            Mockito.when(bookingMapper.toResponseDto(booking))
                    .thenReturn(new BookingResponseDto());

            BookingResponseDto response = bookingService.bookWish(10L);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(WishStatus.BOOKED, wish.getStatus());
        }
    }

    @Test
    void bookWish_own_wish_should_fail() {
        User user = new User();
        user.setId(1L);

        Wish wish = new Wish();
        wish.setOwner(user);
        wish.setStatus(WishStatus.PUBLISHED);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            Mockito.when(wishRepository.findById(1L))
                    .thenReturn(Optional.of(wish));

            Assertions.assertThrows(RuntimeException.class,
                    () -> bookingService.bookWish(1L));
        }
    }

    @Test
    void cancelBooking_as_owner() {
        User user = new User();
        user.setId(1L);

        Wish wish = new Wish();
        wish.setStatus(WishStatus.BOOKED);

        Booking booking = new Booking();
        booking.setBookedBy(user);
        booking.setWish(wish);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            Mockito.when(bookingRepository.findById(1L))
                    .thenReturn(Optional.of(booking));

            bookingService.cancelBooking(1L);

            Assertions.assertEquals(BookingStatus.CANCELLED, booking.getStatus());
            Assertions.assertEquals(WishStatus.PUBLISHED, wish.getStatus());
        }
    }

    @Test
    void getMyBookings_success() {
        User user = new User();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setBookedBy(user);

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);

            Mockito.when(bookingRepository.findAll())
                    .thenReturn(List.of(booking));
            Mockito.when(bookingMapper.toResponseDto(Mockito.any()))
                    .thenReturn(new BookingResponseDto());

            List<BookingResponseDto> result = bookingService.getMyBookings();

            Assertions.assertEquals(1, result.size());
        }
    }
}

