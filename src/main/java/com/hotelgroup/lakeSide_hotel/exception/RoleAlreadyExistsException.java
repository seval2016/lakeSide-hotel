package com.hotelgroup.lakeSide_hotel.exception;

/**
 * @author seval
 */
public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}