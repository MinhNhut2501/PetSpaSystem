package com.petspa.catalog_service.service.Impl;

import com.petspa.catalog_service.config.CatalogProducer;
import com.petspa.catalog_service.dto.request.ServiceRequest;
import com.petspa.catalog_service.dto.response.ServiceResponse;
import com.petspa.catalog_service.entity.ServiceCatalog;
import com.petspa.catalog_service.mapper.ServiceCatalogMapper;
import com.petspa.catalog_service.repository.ServiceCatalogRepository;
import com.petspa.catalog_service.service.ServiceCatalogService;
import com.petspa.common_service.dto.CatalogDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceCatalogRepository repository;
    private final ServiceCatalogMapper mapper;
    private final CatalogProducer catalogProducer;

    @Override
    public ServiceResponse createService(ServiceRequest request) {
        ServiceCatalog entity = mapper.toEntity(request);
        entity.setId(UUID.randomUUID().toString());
        ServiceCatalog saved = repository.save(entity);
        CatalogDocument catalogDocument = CatalogDocument.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .duration(saved.getDuration())
                .build();
        catalogProducer.sendCreateOrUpdateMessage(catalogDocument);
        return mapper.toResponse(saved);
    }

    @Override
    public ServiceResponse updateService(String id, ServiceRequest request) {
        CatalogDocument catalogDocument = CatalogDocument.builder()
                .id(id)
                .name(request.getName())
                .price(request.getPrice())
                .duration(request.getDuration())
                .build();
        catalogProducer.sendCreateOrUpdateMessage(catalogDocument);
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setPrice(request.getPrice());
                    existing.setDuration(request.getDuration());
                    ServiceCatalog updated = repository.save(existing);
                    return mapper.toResponse(updated);
                })
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    @Override
    public void deleteService(String id) {
        catalogProducer.sendDeleteMessage(id);
        repository.deleteById(id);
    }

    @Override
    public ServiceResponse getServiceById(String id) {
        ServiceCatalog found = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return mapper.toResponse(found);
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
