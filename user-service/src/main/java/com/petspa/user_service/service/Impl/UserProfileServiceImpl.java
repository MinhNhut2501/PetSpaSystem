package com.petspa.user_service.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.petspa.common_service.exception.ExistException;
import com.petspa.user_service.config.MinioConfig;
import com.petspa.user_service.dto.request.RegisterUserProfileRequest;
import com.petspa.user_service.dto.response.UserExcelDTO;
import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.mapper.UserProfileMapper;
import com.petspa.user_service.repository.UserProfileReppository;
import com.petspa.user_service.service.UserProfileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileReppository userProfileReppository;
    UserProfileMapper userProfileMapper;
    MinioClient minioClient;
    MinioConfig config;

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

    @Override
    public void exportExcelInVirtualThread(HttpServletResponse response) throws InterruptedException {
        // Cấu hình header response trước
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("danh_sach_nguoi_dung", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

        // Tạo latch để đợi virtual thread xử lý xong
        CountDownLatch latch = new CountDownLatch(1);

        // Tạo và chạy virtual thread
        Thread.startVirtualThread(() -> {
            try {
                System.out.println("▶️ Đang chạy trong virtual thread: " + Thread.currentThread());

                // 1. Lấy dữ liệu từ DB (blocking)
                List<UserProfileEntity> users = userProfileReppository.findAll();
                List<UserExcelDTO> data = users.stream()
                        .map(UserExcelDTO::from)
                        .toList();

                // 2. Ghi vào response output (blocking)
                EasyExcel.write(response.getOutputStream(), UserExcelDTO.class)
                        .sheet("Người dùng")
                        .doWrite(data);

            } catch (Exception e) {
                throw new RuntimeException("Export Excel thất bại", e);
            } finally {
                latch.countDown(); // báo là xong
            }
        });

        // Chờ thread ảo xử lý xong
        latch.await();
    }

    @Override
    public void importExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<UserExcelDTO> dtoList = EasyExcel.read(inputStream)
                    .head(UserExcelDTO.class)
                    .sheet()
                    .doReadSync();

            List<UserProfileEntity> entities = dtoList.stream()
                    .map(userProfileMapper::toUserProfileEntityFromUserExcelDTO)
                    .toList();

            userProfileReppository.saveAll(entities);

        } catch (IOException e) {
            throw new RuntimeException("Import Excel thất bại", e);
        }
    }

    @Override
    public String uploadAvatar(MultipartFile file, String userId) {
        try {
            String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = "avatar_" + userId + "_" + System.currentTimeMillis() + "." + fileExt;

            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(config.getBucket())
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String avatarUrl = config.getPublicUrl() + "/" + config.getBucket() + "/" + fileName;

            // 🔥 Lưu vào DB
            Long id = Long.parseLong(userId);
            userProfileReppository.findById(id).ifPresent(profile -> {
                profile.setAvatarUrl(avatarUrl);
                userProfileReppository.save(profile);
            });

            return avatarUrl;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading avatar", e);
        }
    }

}
