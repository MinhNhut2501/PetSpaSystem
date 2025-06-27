package com.petspa.user_service.mapper;

import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    RegisterUserProfileRequest toRegisterUserProfileRequest(UserProfileEntity userProfile);
    UserProfileEntity toUserProfileEntity(RegisterUserProfileRequest userProfile);
}
