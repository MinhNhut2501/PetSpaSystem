package petspa.search_service.service;

import com.petspa.common_service.dto.CatalogDocument;

public interface CatalogSearchService {
    void upsertCatalog(CatalogDocument catalog);
    void deleteCatlogById(String catalogId);
}
