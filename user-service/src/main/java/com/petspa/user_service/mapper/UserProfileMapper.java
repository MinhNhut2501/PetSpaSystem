package com.petspa.user_service.mapper;

import com.petspa.user_service.dto.response.UserExcelDTO;
import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    RegisterUserProfileRequest toRegisterUserProfileRequest(UserProfileEntity userProfile);
    UserProfileEntity toUserProfileEntity(RegisterUserProfileRequest userProfile);

    @Mapping(target = "id", ignore = true) // hoặc map nếu cần
    UserProfileEntity toUserProfileEntityFromUserExcelDTO(UserExcelDTO userExcelDTO);
}
