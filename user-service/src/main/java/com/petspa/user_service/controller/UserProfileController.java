package com.petspa.user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petspa.common_service.dto.RestResponse;
import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import com.petspa.user_service.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/export-virtual")
    @Operation(summary = "Export user profile Excel", description = "Trả về file Excel (.xlsx)")
    @ApiResponse(responseCode = "200", description = "Excel file",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    public void exportExcelInVirtualThread(HttpServletResponse response) throws InterruptedException {
        userProfileService.exportExcelInVirtualThread(response);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import user Excel", description = "Nhận file Excel (.xlsx) và import vào DB")
    public ResponseEntity<RestResponse<String>> importUserExcel(@RequestPart("file") MultipartFile file) {
        userProfileService.importExcel(file);
        return ResponseEntity.ok(RestResponse.<String>builder()
                .code(200)
                .message("Import thành công")
                .data("OK")
                .build());
    }
}
