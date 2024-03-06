package com.iablonski.processing.service;

import com.iablonski.processing.domain.User;
import com.iablonski.processing.dto.UserDTO;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getAllUsersList();
    void giveOperatorRoleToUser(UUID userId);
    User getUserFromPrincipal(Principal principal);
}
