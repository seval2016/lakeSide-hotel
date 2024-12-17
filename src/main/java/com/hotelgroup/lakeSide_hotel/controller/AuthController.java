package com.hotelgroup.lakeSide_hotel.controller;

import com.hotelgroup.lakeSide_hotel.exception.UserAlreadyExistsException;

import com.hotelgroup.lakeSide_hotel.model.User;
import com.hotelgroup.lakeSide_hotel.request.LoginRequest;
import com.hotelgroup.lakeSide_hotel.response.JwtResponse;
import com.hotelgroup.lakeSide_hotel.security.jwt.JwtUtils;
import com.hotelgroup.lakeSide_hotel.security.user.HotelUserDetails;
import com.hotelgroup.lakeSide_hotel.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author seval
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user){
        try{
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successfully");
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails=(HotelUserDetails) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt,
                roles
        ));
    }

}
