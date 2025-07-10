package com.petspa.catalog_service.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {
    private String name;
    private BigDecimal price;
    private Integer duration;
}