package com.hotelgroup.lakeSide_hotel.service;

import com.hotelgroup.lakeSide_hotel.model.BookedRoom;

import java.util.List;

/**
 * @author seval
 */
public interface IBookingService {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    List<BookedRoom> getBookingsByUserEmail(String email);
}
