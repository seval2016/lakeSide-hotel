package com.hotelgroup.lakeSide_hotel.exception;

/**
 * @author seval
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
