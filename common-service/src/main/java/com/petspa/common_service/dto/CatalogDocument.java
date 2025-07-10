package com.petspa.common_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "catalog_documents")
public class CatalogDocument implements Serializable {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer duration;
}
