package com.petspa.booking_service;

import com.petspa.common_service.enumration.DayOfWeek;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(DayOfWeek.SATURDAY.toString());
    }
}
