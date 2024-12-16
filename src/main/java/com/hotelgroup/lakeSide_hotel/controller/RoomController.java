package com.hotelgroup.lakeSide_hotel.controller;

import com.hotelgroup.lakeSide_hotel.exception.PhotoRetrievalException;
import com.hotelgroup.lakeSide_hotel.exception.ResourceNotFoundException;
import com.hotelgroup.lakeSide_hotel.model.BookedRoom;
import com.hotelgroup.lakeSide_hotel.model.Room;
import com.hotelgroup.lakeSide_hotel.response.BookingResponse;
import com.hotelgroup.lakeSide_hotel.response.RoomResponse;
import com.hotelgroup.lakeSide_hotel.service.IBookingService;
import com.hotelgroup.lakeSide_hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // Bu sınıfın bir Spring MVC Controller olduğunu belirtir.
@RequestMapping("/rooms") // Bu controller için root URL'yi belirler.
@RequiredArgsConstructor // Lombok anotasyonu, final alanlar için constructor oluşturur.
public class RoomController {

    private final IRoomService roomService; // IRoomService servisi dependency injection ile alınır.
    private final IBookingService bookingService; // IBookingService servisi dependency injection ile alınır.

    @PostMapping("/add/new-room") // Yeni bir oda eklemek için HTTP POST metodunu kullanır.
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yalnızca adminlerin bu metodu çalıştırmasına izin verir.
    public ResponseEntity<Room> addNewRoom(
            @RequestParam("photo") MultipartFile photo, // Fotoğraf dosyasını alır.
            @RequestParam("roomType") String roomType, // Oda tipini alır.
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {

        // Yeni bir oda ekler ve ResponseEntity ile geri döner.
        return ResponseEntity.ok(roomService.addNewRoom(photo, roomType, roomPrice));
    }

    @GetMapping("/room/types") // Tüm oda tiplerini almak için HTTP GET metodunu kullanır.
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes(); // Servisten oda tiplerini alır.
    }

    @GetMapping("/all-rooms") // Tüm odaları listelemek için HTTP GET metodunu kullanır.
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms(); // Servisten tüm odaları alır.
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes); // Fotoğrafı Base64 formatına dönüştürür.
                RoomResponse roomResponse = getRoomResponse(room); // Oda yanıtını oluşturur.
                roomResponse.setPhoto(base64Photo); // Fotoğrafı yanıtla ilişkilendirir.
                roomResponses.add(roomResponse); // Oda yanıtlarını ekler.
            }
        }
        return ResponseEntity.ok(roomResponses); // Yanıtı döner.
    }

    @DeleteMapping("/delete/room/{roomId}") // Oda silme işlemi için HTTP DELETE metodunu kullanır.
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yalnızca adminlerin bu işlemi yapmasına izin verir.
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId); // Odayı siler.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Silme işlemi sonrası 204 No Content döner.
    }

    @PutMapping("/update/{roomId}") // Oda güncelleme işlemi için HTTP PUT metodunu kullanır.
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yalnızca adminlerin bu işlemi yapmasına izin verir.
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {
        // Fotoğraf bytes'ları alır ve fotoğraf Blob'a dönüştürülür.
        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes); // Odayı günceller.
        theRoom.setPhoto(photoBlob); // Fotoğrafı güncellenmiş oda nesnesine ekler.
        RoomResponse roomResponse = getRoomResponse(theRoom); // Oda yanıtını oluşturur.
        return ResponseEntity.ok(roomResponse); // Güncellenmiş oda yanıtını döner.
    }

    @GetMapping("/room/{roomId}") // Odayı ID'ye göre almak için HTTP GET metodunu kullanır.
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId); // Odayı ID ile alır.
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room); // Oda yanıtını oluşturur.
            return ResponseEntity.ok(Optional.of(roomResponse)); // Oda yanıtını döner.
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found")); // Oda bulunamazsa hata fırlatır.
    }

    @GetMapping("/available-rooms") // Tarihe ve odaya göre uygun odaları almak için HTTP GET metodunu kullanır.
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType) throws SQLException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType); // Uygun odaları alır.
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : availableRooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes); // Fotoğrafı Base64 formatına dönüştürür.
                RoomResponse roomResponse = getRoomResponse(room); // Oda yanıtını oluşturur.
                roomResponse.setPhoto(photoBase64); // Fotoğrafı yanıtla ilişkilendirir.
                roomResponses.add(roomResponse); // Oda yanıtlarını ekler.
            }
        }
        if (roomResponses.isEmpty()) {
            return ResponseEntity.noContent().build(); // Eğer uygun oda yoksa 204 döner.
        } else {
            return ResponseEntity.ok(roomResponses); // Uygun odaları döner.
        }
    }

    private RoomResponse getRoomResponse(Room room) {
        // Odanın rezervasyonlarını alır ve yanıt olarak döner.
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings.stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(), booking.getCheckOutDate(),
                        booking.getBookingConfirmationCode()))
                .toList();

        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto(); // Odanın fotoğrafını alır.
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo"); // Fotoğraf alınırken hata fırlatılır.
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes, bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId); // Odanın tüm rezervasyonlarını alır.
    }
}
