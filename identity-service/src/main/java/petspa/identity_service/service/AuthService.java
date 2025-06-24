package petspa.identity_service.service;

import petspa.identity_service.dto.response.AuthResponse;
import petspa.identity_service.dto.request.LoginRequest;
import petspa.identity_service.dto.request.RegisterRequest;
import petspa.identity_service.dto.response.RegisterResponse;
import petspa.identity_service.entity.User;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    public AuthResponse login(LoginRequest request);
}
