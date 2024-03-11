package com.iablonski.processing.repository;

import com.iablonski.processing.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
    Role findRoleByName(String name);
}