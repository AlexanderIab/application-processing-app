package com.iablonski.processing.dto;

import com.iablonski.processing.domain.StatusEnum;
import com.iablonski.processing.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestDetailsDTO(UUID id,
                                String text,
                                String phoneNumber,
                                StatusEnum status,
                                LocalDateTime createdAt,
                                User user) {
}
