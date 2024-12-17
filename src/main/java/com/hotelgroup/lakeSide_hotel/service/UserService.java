package com.hotelgroup.lakeSide_hotel.service;

import com.hotelgroup.lakeSide_hotel.exception.UserAlreadyExistsException;
import com.hotelgroup.lakeSide_hotel.model.Role;
import com.hotelgroup.lakeSide_hotel.model.User;
import com.hotelgroup.lakeSide_hotel.repository.RoleRepository;
import com.hotelgroup.lakeSide_hotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author seval
 */

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final RoleRepository roleRepository;

    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + "already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole=roleRepository.findByName("ROLE_USER").orElse(null);
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(String email) {
            User theUser=getUser(email);
            if(theUser != null){
                userRepository.deleteByEmail(email);
            }
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new UserAlreadyExistsException("user not found"));
    }
}
