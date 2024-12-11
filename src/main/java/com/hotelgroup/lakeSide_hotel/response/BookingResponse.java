package com.hotelgroup.lakeSide_hotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author seval
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate chackOutDate;

    private String guestName;

    private String guestEmail;

    private int numOfChildren;

    private int numOfAdults;

    private int totalNumOfGuest;

    private String bookingConfirmationCode;
    private RoomResponse room;


    public BookingResponse(Long id, LocalDate checkInDate, LocalDate chackOutDate, String bookingConfirmationCode) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.chackOutDate = chackOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
