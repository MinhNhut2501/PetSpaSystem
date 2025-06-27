package com.petspa.user_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegisterUserProfileRequest {
    String fullName;
    String email;
    String phone;
    String address;
    String gender;
    LocalDate birthDate;
}
