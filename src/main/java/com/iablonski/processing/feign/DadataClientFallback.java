package com.iablonski.processing.feign;

import com.iablonski.processing.dto.PhoneDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class DadataClientFallback implements DadataClient{

    @Override
    public ResponseEntity<List<PhoneDetailsDTO>> cleanPhoneNumber(List<String> query, String authorization, String secret) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}