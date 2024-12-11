package com.hotelgroup.lakeSide_hotel.service;

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

/**
 * @author seval
 */
@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {


        Room room = new Room(); // Yeni oda nesnesi oluşturuluyor
        room.setRoomType(roomType); // Oda tipi ayarlanıyor
        room.setRoomPrice(roomPrice); // Oda fiyatı ayarlanıyor

        // Fotoğraf varsa, fotoğrafı Blob formatında veritabanına kaydediyoruz
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes(); // Fotoğraf bytes olarak alınıyor
            Blob photoBlob = new SerialBlob(photoBytes); // Fotoğraf Blob'a dönüştürülüyor
            room.setPhoto(photoBlob); // Oda fotoğrafı ekleniyor
        }

        // Oda nesnesini veritabanına kaydediyoruz
        return roomRepository.save(room);
    }
}
