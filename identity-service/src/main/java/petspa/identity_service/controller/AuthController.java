package petspa.identity_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petspa.identity_service.dto.request.LoginRequest;
import petspa.identity_service.dto.request.RegisterRequest;
import petspa.identity_service.dto.response.AuthResponse;
import petspa.identity_service.dto.response.RegisterResponse;
import petspa.identity_service.dto.response.RestResponse;
import petspa.identity_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RestResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(RestResponse.<RegisterResponse>builder()
                .code(HttpStatus.OK.value())
                .data(authService.register(request))
                .message("User registered successfully")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(RestResponse.<AuthResponse>builder()
                .code(HttpStatus.OK.value())
                .data(authService.login(request))
                .message("Login successful")
                .build());
    }


}
