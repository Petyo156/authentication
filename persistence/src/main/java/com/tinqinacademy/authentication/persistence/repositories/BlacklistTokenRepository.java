package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.entities.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, UUID> {
    Optional<BlacklistToken> findByToken(String token);
}