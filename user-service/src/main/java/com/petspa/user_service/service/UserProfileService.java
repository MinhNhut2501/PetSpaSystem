package com.petspa.user_service.service;

import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    String saveUserProfile(RegisterUserProfileRequest request);

    String updateUserProfile(RegisterUserProfileRequest request);

    UserProfileEntity getUserProfileById(Long id);

    void exportExcelInVirtualThread(HttpServletResponse response) throws InterruptedException;

    void importExcel(MultipartFile file);

    String uploadAvatar(MultipartFile file, String userId);

}
