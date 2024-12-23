package com.hotelgroup.lakeSide_hotel.service;

import com.hotelgroup.lakeSide_hotel.model.Role;
import com.hotelgroup.lakeSide_hotel.model.User;

import java.util.List;

/**
 * @author seval
 */
public interface IRoleService {
    List<Role> getRoles();
    Role createRole(Role role);

    void deleteRole(Long id);
    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
