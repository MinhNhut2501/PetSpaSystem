package com.petspa.user_service.service.Impl;

import com.petspa.common_service.exception.ExistException;
import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import com.petspa.user_service.mapper.UserProfileMapper;
import com.petspa.user_service.repository.UserProfileReppository;
import com.petspa.user_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileReppository userProfileReppository;
    UserProfileMapper userProfileMapper;

    @Override
    public String saveUserProfile(RegisterUserProfileRequest request) {
        if (userProfileReppository.findByEmail(request.getEmail()).isPresent()) {
            throw new ExistException(request.getEmail(), "Email already exists");
        }
        UserProfileEntity userProfileEntity = UserProfileEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .build();
        userProfileReppository.save(userProfileEntity);
        return "User Profile Created Success";
    }

    @Override
    public String updateUserProfile(RegisterUserProfileRequest request) {
        UserProfileEntity userProfile = userProfileReppository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Profile not found with ID: " + request.getEmail()));
        userProfile.setAddress(request.getAddress());
        userProfile.setEmail(request.getEmail());
        userProfile.setFullName(request.getFullName());
        userProfile.setPhone(request.getPhone());
        userProfile.setGender(request.getGender());
        userProfile.setBirthDate(request.getBirthDate());
        userProfileReppository.save(userProfile);
        return "User Profile With ID: {} Updated Successfully" + request.getEmail();
    }

    @Override
    public UserProfileEntity getUserProfileById(Long id) {
        return userProfileReppository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Profile not found with ID: " + id));
    }
}
