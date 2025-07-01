package petspa.search_service.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petspa.search_service.entity.UserSearchDocument;
import petspa.search_service.repository.UserSearchRepository;
import petspa.search_service.service.UserSearchService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSearchServiceImpl implements UserSearchService {
    private final UserSearchRepository userSearchRepository;

    @Override
    public Optional<UserSearchDocument> getUserContext(String userId) {
        return userSearchRepository.findById(userId);
    }

    @Override
    public UserSearchDocument saveOrUpdate(UserSearchDocument doc) {
        return userSearchRepository.save(doc);
    }
}
