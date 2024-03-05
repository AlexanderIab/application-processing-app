package com.iablonski.processing.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String text;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String status;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
