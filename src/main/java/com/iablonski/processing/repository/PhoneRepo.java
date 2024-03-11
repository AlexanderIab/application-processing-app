package com.iablonski.processing.repository;

import com.iablonski.processing.domain.PhoneDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepo extends JpaRepository<PhoneDetails, UUID> {
}