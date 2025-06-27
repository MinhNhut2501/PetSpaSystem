package petspa.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.net.URI;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    int status;
    URI type;
    String title;
    String detail;
    URI instance;
    String code;
    List<RestViolation> violations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestViolation {
        String field;
        List<String> messages;
    }
}