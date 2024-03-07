package com.iablonski.processing.service.serviceImpl;

import com.iablonski.processing.domain.Role;
import com.iablonski.processing.domain.User;
import com.iablonski.processing.exception.UserNotFoundException;
import com.iablonski.processing.repository.RoleRepo;
import com.iablonski.processing.dto.UserDTO;
import com.iablonski.processing.mapper.UserMapper;
import com.iablonski.processing.repository.UserRepo;
import com.iablonski.processing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final RoleRepo roleRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.roleRepo = roleRepo;
    }

    @Override
    public List<UserDTO> getAllUsersList() {
        return userRepo.findAll().stream().map(userMapper::toUserDTO).toList();
    }

    @Override

    public void giveOperatorRoleToUser(UUID userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not exist"));
        Set<Role> userRoles = user.getRoles();
        Role role = roleRepo.findRoleByName("OPERATOR");
        userRoles.add(role);
        userRepo.save(user);
    }

    @Override
    public User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepo.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}