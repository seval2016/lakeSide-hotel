package com.hotelgroup.lakeSide_hotel.response;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;// Unique identifier for the room.

    private String roomType;// Type of the room (e.g., Single, Double, Suite).

    private BigDecimal roomPrice;// Price of the room per night.

    private boolean isBooked; // Indicates if the room is currently booked.

    private String photo; // Base64-encoded photo of the room.

    private List<BookingResponse> bookings; // List of bookings associated with the room.

   // Constructor with parameters to initialize the RoomResponse object.
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,
                        byte[] photoBytes, List<BookingResponse> bookings) {
        this.id = id;  // Assigns the unique identifier.
        this.roomType = roomType;// Assigns the type of the room.
        this.roomPrice = roomPrice;// Assigns the room price.
        this.isBooked = isBooked;// Assigns the booking status.
        // Encodes `photoBytes` into Base64 format if not null. Otherwise, assigns null to `photo`.
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.bookings = bookings;// Assigns the list of bookings.

    }


}
