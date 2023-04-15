package com.example.blps_lab1.repository.basic;

import com.example.blps_lab1.model.basic.RefreshToken;
import com.example.blps_lab1.model.basic.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    int deleteByUser(User user);
}
