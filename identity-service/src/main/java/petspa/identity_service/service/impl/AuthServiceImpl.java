package petspa.identity_service.service.impl;

import com.petspa.common_service.exception.ExistException;
import com.petspa.common_service.exception.NotFoundException;
import com.petspa.common_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import petspa.identity_service.dto.response.AuthResponse;
import petspa.identity_service.dto.request.LoginRequest;
import petspa.identity_service.dto.request.RegisterRequest;
import petspa.identity_service.dto.response.RegisterResponse;
import petspa.identity_service.entity.User;
import petspa.identity_service.mapper.UserMapper;
import petspa.identity_service.repository.UserRepository;
import petspa.identity_service.service.AuthService;
import petspa.identity_service.utils.JwtUtil;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    UserMapper userMapper;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ExistException(request.getUsername(), "Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("USER"))
                .enabled(true)
                .build();

        userRepository.save(user);
        return userMapper.toRegisterResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException(HttpStatus.SC_NOT_FOUND,
                        String.valueOf(HttpStatus.SC_UNAUTHORIZED), "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException(user.getUsername(),"password", "Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token);
    }

}
