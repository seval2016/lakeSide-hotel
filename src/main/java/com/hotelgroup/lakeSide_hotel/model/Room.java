package com.hotelgroup.lakeSide_hotel.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_price", nullable = false)
    private BigDecimal roomPrice;

    @Column(name = "is_booked")
    private boolean isBooked = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings = new ArrayList<>();


    public void addBooking(BookedRoom booking){
        if(bookings== null){
            bookings=new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
    }
}
