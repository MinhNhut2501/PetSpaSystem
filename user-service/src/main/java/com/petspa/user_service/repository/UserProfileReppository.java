package com.petspa.user_service.repository;

import com.petspa.user_service.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileReppository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByEmail(String email);
}
