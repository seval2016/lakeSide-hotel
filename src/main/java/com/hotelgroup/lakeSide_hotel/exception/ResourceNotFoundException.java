package com.hotelgroup.lakeSide_hotel.exception;

/**
 * @author seval
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}