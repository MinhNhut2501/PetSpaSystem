package petspa.identity_service.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import petspa.identity_service.config.property.UserProfileProperty;
import petspa.identity_service.dto.request.RegisterUserProfileRequest;

@Component
@RequiredArgsConstructor
public class UserProfileClient {
    private final UserProfileProperty userProfileProperty;
    private RestClient restClient;
    private final RestClient.Builder restClientBuilder;

    @PostConstruct
    private void initialize() {
        this.restClient = restClientBuilder
                .build();
    }

    public String createUserProfile(RegisterUserProfileRequest request) {
       return restClient
                .post()
                .uri(userProfileProperty.urlCreateUser())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<String>() {
                });
    }
}
