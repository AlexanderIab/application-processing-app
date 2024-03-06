package com.iablonski.processing.dto;

import java.util.UUID;

public record RequestDTO(UUID id, String text, String phoneNumber) {
}
