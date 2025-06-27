package com.petspa.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Map 1-1 với User trong identity_service

    String fullName;
    @Column(unique = true, nullable = false)
    String email;

    String phone;
    String address;
    String gender;
    LocalDate birthDate;
}
