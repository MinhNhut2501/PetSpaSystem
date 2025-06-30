package petspa.identity_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petspa.identity_service.dto.request.LoginRequest;
import petspa.identity_service.dto.request.RegisterRequest;
import petspa.identity_service.dto.response.AuthResponse;
import petspa.identity_service.dto.response.RegisterResponse;
import petspa.identity_service.dto.response.RestResponse;
import petspa.identity_service.service.AuthService;
import petspa.identity_service.service.VerificationTokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<RestResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) throws JsonProcessingException {
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

    @GetMapping("/verify")
    public ResponseEntity<RestResponse<String>> verifyAccount(@RequestParam("token") String token){
        String result = verificationTokenService.verifyToken(token);

        String redirectUrl = switch (result) {
            case "Account verified successfully!" -> "/thank-you.html";
            case "Account activated." -> "/already-activated.html";
            case "Link has expired." -> "/expired.html";
            case "Invalid token." -> "/invalid.html";
            default -> "/error.html";
        };
        return ResponseEntity
                .status(HttpStatus.FOUND) // 302
                .header("Location", redirectUrl)
                .build();
    }



}
