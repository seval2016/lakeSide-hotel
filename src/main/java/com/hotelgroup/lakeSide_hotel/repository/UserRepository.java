package com.hotelgroup.lakeSide_hotel.repository;

import com.hotelgroup.lakeSide_hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author seval
 */
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<User> findByEmail(String email);
}