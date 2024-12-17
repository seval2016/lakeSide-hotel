package com.hotelgroup.lakeSide_hotel.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author seval
 */
@Data
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
