package com.hotelgroup.lakeSide_hotel.controller;


import com.hotelgroup.lakeSide_hotel.model.Room;
import com.hotelgroup.lakeSide_hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;


    // POST isteği ile yeni oda ekler
    @PostMapping("/add/new-room") //http://localhost:9192/rooms/add/new-room
    public ResponseEntity<Room> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {

        // Odanın kaydını alır ve ResponseEntity ile geri döner
        return ResponseEntity.ok(roomService.addNewRoom(photo, roomType, roomPrice));
    }
}

