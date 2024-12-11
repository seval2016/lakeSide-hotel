package com.hotelgroup.lakeSide_hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a booked room entity in the hotel reservation system.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_in_date", nullable = false)
    @NotNull(message = "Check-in date cannot be null")
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    @NotNull(message = "Check-out date cannot be null")
    private LocalDate checkOutDate;

    @Column(name = "guest_fullName", nullable = false)
    @NotBlank(message = "Guest full name cannot be empty")
    private String guestFullName;

    @Column(name = "guest_email", nullable = false)
    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Guest email cannot be empty")
    private String guestEmail;

    @Column(name = "children", nullable = false)
    @Min(value = 0, message = "Number of children cannot be negative")
    private int numOfChildren;

    @Column(name = "adults", nullable = false)
    @Min(value = 1, message = "At least one adult is required")
    private int numOfAdults;

    @Transient
    private int totalNumOfGuest;

    @Column(name = "confirmation_code", nullable = false, unique = true)
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull(message = "Room information is required")
    private Room room;

    public void calculateTotalNumberOfGuest() {
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    @PrePersist
    public void generateBookingConfirmationCode() {
        if (this.bookingConfirmationCode == null || this.bookingConfirmationCode.isEmpty()) {
            this.bookingConfirmationCode = UUID.randomUUID().toString();
        }
    }
}

