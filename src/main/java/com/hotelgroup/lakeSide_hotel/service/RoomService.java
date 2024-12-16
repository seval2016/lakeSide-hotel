package com.hotelgroup.lakeSide_hotel.service;

import com.hotelgroup.lakeSide_hotel.exception.InternalServerException;
import com.hotelgroup.lakeSide_hotel.exception.ResourceNotFoundException;
import com.hotelgroup.lakeSide_hotel.model.Room;
import com.hotelgroup.lakeSide_hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author seval
 */
@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    // RoomRepository nesnesi, veritabanı işlemleri için kullanılır.
    private final RoomRepository roomRepository;

    // Yeni bir oda ekler
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType); // Odanın türünü ayarlıyoruz
        room.setRoomPrice(roomPrice); // Odanın fiyatını ayarlıyoruz

        // Eğer dosya boş değilse, fotoğraf verisini alıyoruz
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes(); // Fotoğrafın byte verisini alıyoruz
            Blob photoBlob = new SerialBlob(photoBytes); // Fotoğrafı Blob formatında saklıyoruz
            room.setPhoto(photoBlob); // Odaya fotoğrafı ekliyoruz
        }

        // Odayı veritabanına kaydediyoruz ve döndürüyoruz
        return roomRepository.save(room);
    }

    // Tüm oda türlerini getirir
    @Override
    public List<String> getAllRoomTypes() {
        // Farklı oda türlerini sorguluyoruz
        return roomRepository.findDistinctRoomTypes();
    }

    // Tüm odaları getirir
    @Override
    public List<Room> getAllRooms() {
        // Tüm odaları veritabanından alıyoruz
        return roomRepository.findAll();
    }

    // Oda fotoğrafını odanın ID'sine göre getirir
    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        // Odayı ID'ye göre arıyoruz
        Optional<Room> theRoom = roomRepository.findById(roomId);
        // Eğer oda bulunamazsa, hata fırlatıyoruz
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Room not found");
        }

        // Fotoğraf verisini alıyoruz
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null){
            // Fotoğraf varsa, byte dizisi olarak döndürüyoruz
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }

        // Fotoğraf yoksa, null döndürüyoruz
        return null;
    }

    // Odayı siler
    @Override
    public void deleteRoom(Long roomId) {
        // Odayı ID'ye göre arıyoruz
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()){
            // Oda bulunduysa, silme işlemi yapıyoruz
            roomRepository.deleteById(roomId);
        }
    }

    // Odayı günceller
    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        // Odayı ID'ye göre buluyoruz
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Eğer oda türü sağlanmışsa, güncelliyoruz
        if(roomType != null) room.setRoomType(roomType);

        // Eğer oda fiyatı sağlanmışsa, güncelliyoruz
        if(roomPrice != null) room.setRoomPrice(roomPrice);

        // Eğer fotoğraf sağlanmışsa, fotoğrafı güncelliyoruz
        if(photoBytes != null && photoBytes.length > 0){
            try {
                // Fotoğrafı SerialBlob formatında kaydediyoruz
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex){
                // Hata durumunda, içsel sunucu hatası fırlatıyoruz
                throw new InternalServerException("Failed updating room");
            }
        }

        // Odayı veritabanına kaydediyoruz ve döndürüyoruz
        return roomRepository.save(room);
    }

    // Odayı ID'sine göre getirir
    @Override
    public Optional<Room> getRoomById(Long roomId) {
        // Odayı ID'sine göre buluyoruz
        return Optional.of(roomRepository.findById(roomId)).get();
    }

    // Belirtilen tarihler arası ve oda türüne göre mevcut odaları getirir
    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        // Verilen tarihler ve oda türüne göre odaları sorguluyoruz
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}
