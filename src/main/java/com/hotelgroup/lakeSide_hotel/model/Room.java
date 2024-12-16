package com.hotelgroup.lakeSide_hotel.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Represents a hotel room entity.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA için gerekli varsayılan constructor
@Builder(toBuilder = true)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_type", nullable = false)
    @NonNull
    private String roomType;

    @Column(name = "room_price", nullable = false)
    @NonNull
    private BigDecimal roomPrice;

    @Column(name = "is_booked", nullable = false)
    private boolean isBooked = false;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedRoom booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        if (booking.getBookingConfirmationCode() == null || booking.getBookingConfirmationCode().isEmpty()) {
            booking.setBookingConfirmationCode(UUID.randomUUID().toString());
            // UUID'yi burada da ekleyebilirsiniz.
        }
    }

    public void removeBooking(BookedRoom booking) {
        if (bookings != null && bookings.contains(booking)) {
            bookings.remove(booking);
            booking.setRoom(null);
        }
    }

    public void addBookings(BookedRoom booking){
        if(bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }
}