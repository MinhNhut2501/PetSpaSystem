package petspa.search_service.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import petspa.search_service.entity.UserSearchDocument;

@Repository
public interface UserSearchRepository extends ElasticsearchRepository<UserSearchDocument, String> {
}
