package petspa.search_service.service.Impl;

import com.petspa.common_service.dto.CatalogDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import petspa.search_service.service.CatalogSearchService;
@RequiredArgsConstructor
@Service
@Slf4j
public class CatalogSearchServiceImpl implements CatalogSearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    @Override
    public void upsertCatalog(CatalogDocument catalog) {
        // 1. Tạo index nếu chưa tồn tại
        IndexOperations indexOps = elasticsearchOperations.indexOps(CatalogDocument.class);
        if (!indexOps.exists()) {
            indexOps.create();
            Document mapping = indexOps.createMapping();
            indexOps.putMapping(mapping);
        }

        // 2. Tạo hoặc cập nhật document
        IndexQuery query = new IndexQueryBuilder()
                .withId(catalog.getId())
                .withObject(catalog)
                .build();

        elasticsearchOperations.index(query, elasticsearchOperations.getIndexCoordinatesFor(CatalogDocument.class));
        log.info("Upserted booking with ID: {}", catalog.getId());
    }

    @Override
    public void deleteCatlogById(String catalogId) {
        elasticsearchOperations.delete(catalogId, CatalogDocument.class);
        log.info("Deleted Booking with ID: {}", catalogId);
    }
}
