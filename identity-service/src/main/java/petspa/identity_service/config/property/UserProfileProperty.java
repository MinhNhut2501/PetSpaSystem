package petspa.identity_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("user-service")
public record UserProfileProperty (
String urlCreateUser
){}
