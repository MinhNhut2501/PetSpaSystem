package petspa.chat_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import petspa.chat_service.dto.UserSearchDocument;

@FeignClient(name = "search-service")
public interface UserSearchClient {

    @GetMapping("/api/search/user/{userId}")
    UserSearchDocument getUserContext(@PathVariable("userId") String userId);
}

