package com.petspa.user_service.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import com.petspa.user_service.entity.UserProfileEntity;
import com.petspa.user_service.util.CustomLocalDateConverter;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserExcelDTO {
    @ExcelProperty("ID")
    Long id;

    @ExcelProperty("Tên người dùng")
    String fullName;

    @ExcelProperty("Email")
    String email;

    @ExcelProperty("Giới tính")
    String gender;

    @ExcelProperty(value = "Ngày sinh",converter = CustomLocalDateConverter.class)
    LocalDate birthDate;

    @ExcelProperty("Số điện thoại")
    String phone;

    @ExcelProperty("Địa chỉ")
    String address;

    public static UserExcelDTO from(UserProfileEntity user) {
        UserExcelDTO dto = new UserExcelDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setBirthDate(user.getBirthDate());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        return dto;
    }
}
