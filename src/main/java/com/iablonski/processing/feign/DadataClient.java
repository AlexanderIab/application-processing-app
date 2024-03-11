package com.iablonski.processing.feign;

import com.iablonski.processing.dto.PhoneDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "dadata-client", url = "https://cleaner.dadata.ru/api/v1/clean", fallback = DadataClientFallback.class)
public interface DadataClient {

    @PostMapping("/phone")
    ResponseEntity<List<PhoneDetailsDTO>> cleanPhoneNumber(@RequestBody List<String> query,
                                                          @RequestHeader("Authorization") String authorization,
                                                          @RequestHeader("X-Secret") String secret);
}