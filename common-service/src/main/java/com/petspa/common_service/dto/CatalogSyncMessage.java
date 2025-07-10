package com.petspa.common_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatalogSyncMessage {
    private String action; // CREATE, UPDATE, DELETE
    private CatalogDocument catalog; // null nếu action là DELETE
}
