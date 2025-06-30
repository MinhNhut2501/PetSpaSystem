package petspa.identity_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petspa.common_service.dto.NotificationMessage;
import com.petspa.common_service.exception.ExistException;
import com.petspa.common_service.exception.NotFoundException;
import com.petspa.common_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.apache.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import petspa.identity_service.client.UserProfileClient;
import petspa.identity_service.dto.request.LoginRequest;
import petspa.identity_service.dto.request.RegisterRequest;
import petspa.identity_service.dto.request.RegisterUserProfileRequest;
import petspa.identity_service.dto.response.AuthResponse;
import petspa.identity_service.dto.response.ErrorResponse;
import petspa.identity_service.dto.response.RegisterResponse;
import petspa.identity_service.entity.UserEntity;
import petspa.identity_service.entity.VerificationToken;
import petspa.identity_service.exception.UserProfileException;
import petspa.identity_service.mapper.UserMapper;
import petspa.identity_service.repository.UserRepository;
import petspa.identity_service.service.AuthService;
import petspa.identity_service.service.VerificationTokenService;
import petspa.identity_service.utils.JwtUtil;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserProfileClient userProfileClient;
    private final RabbitTemplate rabbitTemplate;
    private final VerificationTokenService verificationTokenService;
    @Value("${spring.rabbitmq.template.exchange}")
    String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    String routingKey;

    @Override
    public RegisterResponse register(RegisterRequest request) throws JsonProcessingException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ExistException(request.getEmail(),"Email: "+request.getEmail()+" already exists");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("USER"))
                .enabled(false)
                .build();

        userRepository.save(userEntity);
        UserEntity user = userRepository.findByEmail(request.getEmail()).get();
        RegisterUserProfileRequest userProfileRequest = RegisterUserProfileRequest.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .build();

        String rawResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        rawResponse = userProfileClient.createUserProfile(userProfileRequest);
        JsonNode root = mapper.readTree(rawResponse);
        int code = root.path("code").asInt();

        if(code == 200){
            VerificationToken verificationToken = verificationTokenService.createTokenForUser(user);
            String activationLink = "http://localhost:8087/api/auth/verify?token=" + verificationToken.getToken();
            NotificationMessage message = NotificationMessage.builder()
                    .email("sandbox.smtp.mailtrap.io")
                    .subject("Xác minh tài khoản PetSpa")
                    .fullName(request.getFullName())
                    .activationLink(activationLink)
                    .build();
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            return userMapper.toRegisterResponse(user);
        }else {
            ErrorResponse errorResponse = mapper.treeToValue(root, ErrorResponse.class);
            throw new UserProfileException(errorResponse);
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException(HttpStatus.SC_NOT_FOUND,
                        String.valueOf(HttpStatus.SC_UNAUTHORIZED), "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new ResourceNotFoundException(userEntity.getEmail(),"password", "Invalid password");
        }

        String token = jwtUtil.generateToken(userEntity.getEmail(), userEntity.getRoles());
        return new AuthResponse(token);
    }

}
