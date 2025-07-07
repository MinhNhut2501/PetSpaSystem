package com.petspa.booking_service.controller;

import com.petspa.booking_service.dto.request.CreateBookingRequest;
import com.petspa.booking_service.dto.request.UpdateBookingStatusRequest;
import com.petspa.booking_service.dto.response.BookingResponse;
import com.petspa.booking_service.entity.Booking;
import com.petspa.booking_service.service.BookingService;
import com.petspa.common_service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking API", description = "Quản lý booking spa thú cưng")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Tạo booking mới")
    @ApiResponse(responseCode = "201", description = "Booking tạo thành công")
    @PostMapping
    public ResponseEntity<RestResponse<BookingResponse>> create(
            @RequestBody @Valid CreateBookingRequest request) {
        BookingResponse created = bookingService.createBooking(request);
        return ResponseEntity.ok(
                RestResponse.<BookingResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Booking created successfully")
                        .data(created)
                        .build()
        );
    }

    @Operation(summary = "Lấy thông tin 1 booking theo ID")
    @ApiResponse(responseCode = "200", description = "Lấy booking thành công")
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<BookingResponse>> get(
            @Parameter(description = "ID của booking") @PathVariable String id) {
        return ResponseEntity.ok(
                RestResponse.<BookingResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("Booking retrieved successfully")
                        .data(bookingService.getBooking(id))
                        .build()
        );
    }

    @Operation(summary = "Lấy danh sách booking theo userId")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse<List<BookingResponse>>> getByUser(
            @Parameter(description = "ID người dùng") @PathVariable String userId) {
        return ResponseEntity.ok(
                RestResponse.<List<BookingResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Bookings retrieved successfully")
                        .data(bookingService.getBookingsByUser(userId))
                        .build()
        );
    }

    @Operation(summary = "Lấy danh sách booking theo status")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/status/{status}")
    public ResponseEntity<RestResponse<List<BookingResponse>>> getByStatus(
            @Parameter(description = "Trạng thái booking (PENDING, CONFIRMED, CANCELLED, ...)") @PathVariable String status) {
        return ResponseEntity.ok(
                RestResponse.<List<BookingResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Bookings retrieved successfully")
                        .data(bookingService.getBookingsByStatus(status))
                        .build()
        );
    }

    @Operation(summary = "Lấy danh sách booking theo khoảng thời gian")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/range")
    public ResponseEntity<RestResponse<List<BookingResponse>>> getByRange(
            @Parameter(description = "Thời gian bắt đầu", example = "2025-07-01T00:00:00")
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "Thời gian kết thúc", example = "2025-07-10T23:59:59")
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(
                RestResponse.<List<BookingResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Bookings in range retrieved successfully")
                        .data(bookingService.getBookingsInTimeRange(from, to))
                        .build()
        );
    }

    @Operation(summary = "Cập nhật trạng thái booking")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    @PutMapping("/{id}/status")
    public ResponseEntity<RestResponse<BookingResponse>> updateStatus(
            @Parameter(description = "ID của booking") @PathVariable String id,
            @RequestBody @Valid UpdateBookingStatusRequest request) {
        return ResponseEntity.ok(
                RestResponse.<BookingResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("Booking status updated successfully")
                        .data(bookingService.updateStatus(id, request.getStatus()))
                        .build()
        );
    }

    @Operation(summary = "Xoá booking theo ID")
    @ApiResponse(responseCode = "204", description = "Xoá thành công")
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> delete(
            @Parameter(description = "ID của booking") @PathVariable String id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(
                RestResponse.<Void>builder()
                        .code(HttpStatus.NO_CONTENT.value())
                        .message("Booking deleted successfully")
                        .build()
        );
    }
}
