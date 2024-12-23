package com.hotelgroup.lakeSide_hotel.service;

import com.hotelgroup.lakeSide_hotel.exception.RoleAlreadyExistsException;
import com.hotelgroup.lakeSide_hotel.exception.UserAlreadyExistsException;
import com.hotelgroup.lakeSide_hotel.model.Role;
import com.hotelgroup.lakeSide_hotel.model.User;
import com.hotelgroup.lakeSide_hotel.repository.RoleRepository;
import com.hotelgroup.lakeSide_hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author seval
 */
@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if(roleRepository.existsByName(roleName)){
            throw new RoleAlreadyExistsException(theRole.getName() + " role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        Optional<User> user = userRepository.findById(userId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent() && role.get().getUsers().contains(user.get())){
            throw new UserAlreadyExistsException(
                    user.get().getFirstName() + " is already assigned to the " + role.get().getName() + " role");
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(Role::removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}
