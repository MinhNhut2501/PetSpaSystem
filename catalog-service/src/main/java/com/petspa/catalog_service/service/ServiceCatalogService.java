package com.petspa.catalog_service.service;

import com.petspa.catalog_service.dto.request.ServiceRequest;
import com.petspa.catalog_service.dto.response.ServiceResponse;

import java.util.List;

public interface ServiceCatalogService {
    ServiceResponse createService(ServiceRequest request);
    ServiceResponse updateService(String id, ServiceRequest request);
    void deleteService(String id);
    ServiceResponse getServiceById(String id);
    List<ServiceResponse> getAllServices();
}
