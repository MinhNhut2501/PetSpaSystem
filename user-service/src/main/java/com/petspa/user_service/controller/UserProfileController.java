package com.petspa.user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petspa.common_service.dto.RestResponse;
import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import com.petspa.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-profile")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<RestResponse<String>> createUserProfile(@RequestBody RegisterUserProfileRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(RestResponse.<String>builder()
                .code(200)
                .message("Successfully")
                .data(userProfileService.saveUserProfile(request))
                .build());
    }

    @PutMapping
    public ResponseEntity<RestResponse<String>> updateUserProfile(@RequestBody RegisterUserProfileRequest request) {
        return ResponseEntity.ok(RestResponse.<String>builder()
                .code(200)
                .message("Updated Successfully")
                .data(userProfileService.updateUserProfile(request))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<UserProfileEntity>> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(RestResponse.<UserProfileEntity>builder()
                .code(200)
                .message("Retrieved Successfully")
                .data(userProfileService.getUserProfileById(id))
                .build());
    }
}
