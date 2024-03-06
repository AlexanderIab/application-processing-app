package com.iablonski.processing.dto;

import com.iablonski.processing.domain.Role;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id, String username, String password, Set<Role> roles) {
}
