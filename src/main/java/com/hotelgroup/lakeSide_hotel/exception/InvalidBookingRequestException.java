package com.hotelgroup.lakeSide_hotel.exception;

/**
 * @author seval
 */
public class InvalidBookingRequestException extends RuntimeException{
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
