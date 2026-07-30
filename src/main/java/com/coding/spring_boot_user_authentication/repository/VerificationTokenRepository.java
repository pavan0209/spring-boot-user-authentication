package com.coding.spring_boot_user_authentication.repository;

import com.coding.spring_boot_user_authentication.entity.VerificationToken;
import com.coding.spring_boot_user_authentication.security.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByTokenAndType(String token, TokenType type);
}