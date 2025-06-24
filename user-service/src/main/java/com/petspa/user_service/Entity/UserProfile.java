package com.petspa.user_service.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile {

    @Id
    Long userId; // Map 1-1 với User trong identity_service

    String fullName;
    String phoneNumber;
    String address;
    String gender;
    String avatarUrl;
}
