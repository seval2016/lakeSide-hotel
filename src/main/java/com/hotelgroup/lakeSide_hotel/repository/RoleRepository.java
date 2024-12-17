package com.hotelgroup.lakeSide_hotel.repository;

import com.hotelgroup.lakeSide_hotel.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String roleUser);
    boolean existsByName(String role);
}
