package petspa.identity_service.mapper;

import org.mapstruct.Mapper;
import petspa.identity_service.dto.response.RegisterResponse;
import petspa.identity_service.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterResponse toRegisterResponse(UserEntity userEntity);
}