package com.petspa.user_service.service;

import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;

public interface UserProfileService {
    String saveUserProfile(RegisterUserProfileRequest request);
    String updateUserProfile(RegisterUserProfileRequest request);
    UserProfileEntity getUserProfileById(Long id);
}
