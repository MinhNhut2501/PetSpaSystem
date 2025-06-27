package petspa.identity_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegisterUserProfileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    String fullName;
    String email;
    String phone;
    String address;
    String gender;
    LocalDate birthDate;
}
