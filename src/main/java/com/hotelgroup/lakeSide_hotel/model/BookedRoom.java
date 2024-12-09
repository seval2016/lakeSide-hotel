package com.hotelgroup.lakeSide_hotel.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long bookingId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "guest_fullName", nullable = false)
    private String guestFullName;

    @Column(name = "guest_email", nullable = false)
    private String guestEmail;

    @Column(name = "children", nullable = false)
    private int numOfChildren;

    @Column(name = "adults", nullable = false)
    private int numOfAdults;

    @Column(name = "total_guest", nullable = false)
    private int totalNumOfGuest;

    @Column(name = "confirmation_code", nullable = false)
    private String bookingConfirmationCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest=this.numOfAdults + numOfChildren;
    }


    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
