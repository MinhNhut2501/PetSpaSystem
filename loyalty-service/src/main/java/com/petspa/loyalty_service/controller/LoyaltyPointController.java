package com.petspa.loyalty_service.controller;
import com.petspa.common_service.dto.RestResponse;
import com.petspa.loyalty_service.entity.LoyaltyPoint;
import com.petspa.loyalty_service.service.LoyaltyPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
@Tag(name = "Loyalty API", description = "Quản lý điểm thưởng khách hàng")
public class LoyaltyPointController {

    private final LoyaltyPointService loyaltyPointService;

    @Operation(summary = "Lấy tổng điểm của user (Redis trước, fallback DB)")
    @ApiResponse(responseCode = "200", description = "Lấy điểm thành công")
    @GetMapping("/points/{userId}")
    public ResponseEntity<RestResponse<Integer>> getPointsByUserId(@PathVariable String userId) {
        Integer points = loyaltyPointService.getPointsByUserId(userId);
        return ResponseEntity.ok(
                RestResponse.<Integer>builder()
                        .code(HttpStatus.OK.value())
                        .message("Lấy tổng điểm thành công")
                        .data(points)
                        .build()
        );
    }

    @Operation(summary = "Lấy bản ghi LoyaltyPoint mới nhất của user")
    @ApiResponse(responseCode = "200", description = "Lấy bản ghi mới nhất thành công")
    @GetMapping("/points/latest/{userId}")
    public ResponseEntity<RestResponse<LoyaltyPoint>> getLatestPointRecord(@PathVariable String userId) {
        LoyaltyPoint latestPoint = loyaltyPointService.getLatestPointRecord(userId);
        return ResponseEntity.ok(
                RestResponse.<LoyaltyPoint>builder()
                        .code(HttpStatus.OK.value())
                        .message("Lấy bản ghi mới nhất thành công")
                        .data(latestPoint)
                        .build()
        );
    }
}