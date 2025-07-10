package com.petspa.catalog_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer duration;
}
