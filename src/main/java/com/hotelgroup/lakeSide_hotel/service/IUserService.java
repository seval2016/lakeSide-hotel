package com.hotelgroup.lakeSide_hotel.service;

import com.hotelgroup.lakeSide_hotel.model.User;

import java.util.List;

/**
 * @author seval
 */
public interface IUserService {
    User registerUser(User user);
    List<User> getUsers();

    void deleteUser(String email);

    User getUser(String email);
}
