package com.petspa.catalog_service.mapper;

import com.petspa.catalog_service.dto.request.ServiceRequest;
import com.petspa.catalog_service.dto.response.ServiceResponse;
import com.petspa.catalog_service.entity.ServiceCatalog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceCatalogMapper {
    ServiceCatalog toEntity(ServiceRequest request);
    ServiceResponse toResponse(ServiceCatalog entity);
}
