package com.hotelgroup.lakeSide_hotel.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author seval
 */

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {
    private Long id;
    private String email;
    private String token;
    private String type="Bearer";
    private List<String> roles;

    public JwtResponse(Long id, String email, String token, List<String> roles) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }
}
