package com.petspa.catalog_service.controller;

import com.petspa.catalog_service.dto.request.ServiceRequest;
import com.petspa.catalog_service.dto.response.ServiceResponse;
import com.petspa.catalog_service.service.ServiceCatalogService;
import com.petspa.common_service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    @Operation(summary = "Tạo dịch vụ mới")
    @ApiResponse(responseCode = "201", description = "Tạo dịch vụ thành công")
    @PostMapping
    public ResponseEntity<RestResponse<ServiceResponse>> createService(
            @RequestBody @Valid ServiceRequest request) {
        ServiceResponse created = serviceCatalogService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                RestResponse.<ServiceResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo dịch vụ thành công")
                        .data(created)
                        .build()
        );
    }

    @Operation(summary = "Cập nhật dịch vụ")
    @ApiResponse(responseCode = "200", description = "Cập nhật dịch vụ thành công")
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<ServiceResponse>> updateService(
            @PathVariable String id,
            @RequestBody @Valid ServiceRequest request) {
        ServiceResponse updated = serviceCatalogService.updateService(id, request);
        return ResponseEntity.ok(
                RestResponse.<ServiceResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("Cập nhật dịch vụ thành công")
                        .data(updated)
                        .build()
        );
    }

    @Operation(summary = "Xoá dịch vụ")
    @ApiResponse(responseCode = "204", description = "Xoá dịch vụ thành công")
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteService(@PathVariable String id) {
        serviceCatalogService.deleteService(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                RestResponse.<Void>builder()
                        .code(HttpStatus.NO_CONTENT.value())
                        .message("Xoá dịch vụ thành công")
                        .build()
        );
    }

    @Operation(summary = "Lấy dịch vụ theo ID")
    @ApiResponse(responseCode = "200", description = "Lấy dịch vụ thành công")
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ServiceResponse>> getServiceById(@PathVariable String id) {
        ServiceResponse found = serviceCatalogService.getServiceById(id);
        return ResponseEntity.ok(
                RestResponse.<ServiceResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("Lấy dịch vụ thành công")
                        .data(found)
                        .build()
        );
    }

    @Operation(summary = "Lấy tất cả dịch vụ")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách dịch vụ thành công")
    @GetMapping
    public ResponseEntity<RestResponse<List<ServiceResponse>>> getAllServices() {
        List<ServiceResponse> services = serviceCatalogService.getAllServices();
        return ResponseEntity.ok(
                RestResponse.<List<ServiceResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách dịch vụ thành công")
                        .data(services)
                        .build()
        );
    }
}
