package com.iablonski.processing.service;

import com.iablonski.processing.domain.PhoneDetails;
import com.iablonski.processing.dto.PhoneDetailsDTO;

import java.util.List;

public interface PhoneDetailsService {
    PhoneDetails createPhoneDetails(String phone);
    List<PhoneDetailsDTO> cleanPhoneNumber(String phoneNumber, String token, String secret);
}