package petspa.identity_service.service;

import petspa.identity_service.entity.UserEntity;
import petspa.identity_service.entity.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createTokenForUser(UserEntity user);
    void deleteToken(VerificationToken token);
    String verifyToken(String token);
}
