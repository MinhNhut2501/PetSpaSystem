package petspa.search_service.service.Impl;

import com.petspa.common_service.dto.BookingDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import petspa.search_service.service.BookingSearchService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingSearchServiceImpl implements BookingSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void upsertBooking(BookingDocument booking) {
        // 1. Tạo index nếu chưa tồn tại
        IndexOperations indexOps = elasticsearchOperations.indexOps(BookingDocument.class);
        if (!indexOps.exists()) {
            indexOps.create();
            Document mapping = indexOps.createMapping();
            indexOps.putMapping(mapping);
        }

        // 2. Tạo hoặc cập nhật document
        IndexQuery query = new IndexQueryBuilder()
                .withId(booking.getBookingId())
                .withObject(booking)
                .build();

        elasticsearchOperations.index(query, elasticsearchOperations.getIndexCoordinatesFor(BookingDocument.class));
        log.info("Upserted booking with ID: {}", booking.getBookingId());
    }

    @Override
    public void deleteBookingById(String bookingId) {
        elasticsearchOperations.delete(bookingId, BookingDocument.class);
        log.info("Deleted Booking with ID: {}", bookingId);
    }
}

