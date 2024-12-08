package com.hotelgroup.lakeSide_hotel.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_room")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_price", nullable = false)
    private BigDecimal roomPrice;

    @Column(name = "is_booked")
    private boolean isBooked = false;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedRoom> bookings = new ArrayList<>();
}
