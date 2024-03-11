package com.iablonski.processing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iablonski.processing.domain.Role;

import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id,
                      String username,
                      @JsonIgnore
                      String password,
                      Set<Role> roles) {
}