package com.petspa.common_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponse<T> {
    int code;
    T data;
    String message;
}
