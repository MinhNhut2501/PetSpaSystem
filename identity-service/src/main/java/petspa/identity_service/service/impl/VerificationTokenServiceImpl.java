package petspa.identity_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import petspa.identity_service.entity.UserEntity;
import petspa.identity_service.entity.VerificationToken;
import petspa.identity_service.repository.UserRepository;
import petspa.identity_service.repository.VerificationTokenRepository;
import petspa.identity_service.service.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenServiceImpl implements VerificationTokenService {
    VerificationTokenRepository verificationTokenRepository;
    UserRepository userRepository;
    @Override
    public VerificationToken createTokenForUser(UserEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24)) // hết hạn sau 24h
                .build();
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void deleteToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    @Override
    public String verifyToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .map(verificationToken -> {
                    if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                        return "Link has expired.";
                    }

                    UserEntity user = verificationToken.getUser();
                    if (user.isEnabled()) {
                        return "Account activated.";
                    }

                    user.setEnabled(true);
                    userRepository.save(user);
                    verificationTokenRepository.delete(verificationToken); // Xoá token sau khi dùng
                    return "Account verified successfully!";
                })
                .orElse("Invalid token.");
    }

}
