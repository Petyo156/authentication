package com.tinqinacademy.authentication.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "confirmation_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;
}
