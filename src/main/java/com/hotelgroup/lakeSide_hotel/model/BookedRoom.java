package com.hotelgroup.lakeSide_hotel.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_bookedRoom")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long bookingId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;

    private int numOfChildren;

    private int numOfAdults;

    private int totalNumOfGuest;

    private String bookingConfirmationCode;


    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest=this.numOfAdults + numOfChildren;
    }


}
