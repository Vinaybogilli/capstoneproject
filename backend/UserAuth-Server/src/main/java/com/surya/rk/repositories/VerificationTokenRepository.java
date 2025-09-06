package com.surya.rk.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.rk.entities.Token;

@Repository
public interface VerificationTokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByToken(String token);
}
