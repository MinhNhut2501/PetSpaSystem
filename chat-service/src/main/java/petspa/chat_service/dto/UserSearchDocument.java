package petspa.chat_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserSearchDocument {
    private String userId;
    private String fullName;
    private List<String> petNames;
    private Integer spaCount;
    private LocalDate lastVisit;
    private String phone;
}