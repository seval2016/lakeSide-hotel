package com.hotelgroup.lakeSide_hotel.repository;

import com.hotelgroup.lakeSide_hotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedRoomRepository extends JpaRepository<BookedRoom,Integer> {
}
