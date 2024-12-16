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
@Entity // Bu sınıfın bir JPA Entity sınıfı olduğunu belirtir.
@Getter // Lombok anotasyonu, getter metodlarını otomatik olarak oluşturur.
@Setter // Lombok anotasyonu, setter metodlarını otomatik olarak oluşturur.
@AllArgsConstructor // Tüm alanları içeren bir constructor oluşturur.
@NoArgsConstructor(force = true) // Varsayılan bir constructor oluşturur. JPA için gereklidir.
@Builder(toBuilder = true) // Builder pattern'ini sağlar, nesne oluşturmayı kolaylaştırır.
public class Room {

    @Id // Bu alanın birincil anahtar olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin otomatik olarak arttırılacağını belirtir.
    private Long id;

    @Column(name = "room_type", nullable = false) // Bu alanın veritabanı kolonunu belirtir.
    @NonNull // Bu alanın null olamayacağını belirtir.
    private String roomType;

    @Column(name = "room_price", nullable = false) // Oda fiyatı için veri tabanı kolonunu belirtir.
    @NonNull // Bu alanın null olamayacağını belirtir.
    private BigDecimal roomPrice;

    @Column(name = "is_booked", nullable = false) // Odanın rezervasyon durumunu belirtir.
    private boolean isBooked = false;

    @Lob // Veritabanında büyük veri tipleri (blob) için kullanılır.
    private Blob photo; // Oda fotoğrafını saklar.

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedRoom> bookings = new ArrayList<>(); // Oda ile ilişkili rezervasyonları tutar.

    // Rezervasyon eklemek için yardımcı metot
    public void addBooking(BookedRoom booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(booking); // Rezervasyonu listeye ekler.
        booking.setRoom(this); // Odanın rezervasyonla ilişkilendirilmesini sağlar.
        isBooked = true; // Odanın rezerve olduğunu belirtir.
        if (booking.getBookingConfirmationCode() == null || booking.getBookingConfirmationCode().isEmpty()) {
            booking.setBookingConfirmationCode(UUID.randomUUID().toString()); // UUID ile rezervasyon onay kodu oluşturur.
        }
    }

    // Rezervasyon kaldırmak için yardımcı metot
    public void removeBooking(BookedRoom booking) {
        if (bookings != null && bookings.contains(booking)) {
            bookings.remove(booking); // Rezervasyonu listeden çıkarır.
            booking.setRoom(null); // Odanın rezervasyonla ilişkisiz kalmasını sağlar.
        }
    }

    // Birden fazla rezervasyon eklemek için metot
    public void addBookings(BookedRoom booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(booking); // Rezervasyonu ekler.
        booking.setRoom(this); // Odanın rezervasyonla ilişkilendirilmesini sağlar.
        isBooked = true; // Odanın rezerve olduğunu belirtir.
        String bookingCode = RandomStringUtils.randomNumeric(10); // Rezervasyon kodu oluşturur.
        booking.setBookingConfirmationCode(bookingCode);
    }
}