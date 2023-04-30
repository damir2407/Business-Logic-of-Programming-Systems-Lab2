package com.example.blps_lab1.model.basic;

import javax.persistence.*;
import lombok.Data;

import java.time.Instant;
//TODO фикс
@Entity(name = "refresh_token")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_login")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false , name = "expiry_date")
    private Instant expiryDate;
}
