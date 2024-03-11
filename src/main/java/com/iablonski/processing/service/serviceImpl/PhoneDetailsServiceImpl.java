package com.iablonski.processing.service.serviceImpl;

import com.iablonski.processing.domain.PhoneDetails;
import com.iablonski.processing.dto.PhoneDetailsDTO;
import com.iablonski.processing.feign.DadataClient;
import com.iablonski.processing.repository.PhoneRepo;
import com.iablonski.processing.service.PhoneDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneDetailsServiceImpl implements PhoneDetailsService {

    private final PhoneRepo phoneRepo;
    private final DadataClient dadataClient;
    @Value("${dadata.token}")
    private String token;
    @Value("${dadata.secret}")
    private String secret;

    @Autowired
    public PhoneDetailsServiceImpl(PhoneRepo phoneRepo, DadataClient dadataClient) {
        this.phoneRepo = phoneRepo;
        this.dadataClient = dadataClient;
    }

    @Override
    public PhoneDetails createPhoneDetails(String phone) {

        List<PhoneDetailsDTO> res = cleanPhoneNumber(phone, token, secret);
        System.out.println(res);
        PhoneDetailsDTO phoneDetailsDTO = res.get(0);
        PhoneDetails phoneDetails = new PhoneDetails();
        phoneDetails.setCityCode(phoneDetailsDTO.city_code());
        phoneDetails.setCountryCode(phoneDetailsDTO.country_code());
        phoneDetails.setPhone(phoneDetailsDTO.phone());
        return phoneRepo.save(phoneDetails);
    }

    @Override
    public List<PhoneDetailsDTO> cleanPhoneNumber(String phoneNumber, String token, String secret) {
        List<String> queries = List.of(phoneNumber);
        System.out.println(queries);
        return dadataClient.cleanPhoneNumber(queries, "Token " + token, secret).getBody();
    }
}