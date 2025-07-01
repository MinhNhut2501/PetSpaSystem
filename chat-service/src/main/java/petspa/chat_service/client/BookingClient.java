package petspa.chat_service.client;

import org.springframework.stereotype.Service;
import petspa.chat_service.dto.BookingInfo;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
@Service
public class BookingClient {

    private static final Map<String, petspa.chat_service.dto.BookingInfo> dummyData = new HashMap<>();

    static {
        // Giả lập user "user1" có lịch spa lúc 10:00
        dummyData.put("user1", new BookingInfo("user1", LocalTime.of(10, 0).toString()));
    }

    public BookingInfo getBookingForToday(String userId) {
        // Trả về lịch nếu user có (giả lập)
        return dummyData.get(userId);
    }
}