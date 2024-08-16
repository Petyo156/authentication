package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.entities.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationCodesRepository extends JpaRepository<ConfirmationCode, UUID> {
    Optional<ConfirmationCode> findByCode(String code);
    Optional<ConfirmationCode> findByUserId(UUID userId);

}
