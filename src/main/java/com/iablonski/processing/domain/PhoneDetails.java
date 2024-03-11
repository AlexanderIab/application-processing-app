package com.iablonski.processing.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "phones")
@Getter
@Setter
@ToString
public class PhoneDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "city_code")
    private String cityCode;
    @Column(name = "phone")
    private String phone;
}