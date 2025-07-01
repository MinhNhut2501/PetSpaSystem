package petspa.search_service.service;

import petspa.search_service.entity.UserSearchDocument;

import java.util.Optional;

public interface UserSearchService {
    Optional<UserSearchDocument> getUserContext(String userId);
    UserSearchDocument saveOrUpdate(UserSearchDocument doc);

}
