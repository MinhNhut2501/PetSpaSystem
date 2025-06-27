package petspa.identity_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import petspa.identity_service.dto.response.AuthResponse;
import petspa.identity_service.dto.request.LoginRequest;
import petspa.identity_service.dto.request.RegisterRequest;
import petspa.identity_service.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request) throws JsonProcessingException;
    public AuthResponse login(LoginRequest request);
}
