package petspa.search_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "user-index")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchDocument {

    @Id
    String userId;

    String fullName;
    List<String> petNames;
    Integer spaCount;

    @Field(type = FieldType.Date, format = DateFormat.date) // ISO date: yyyy-MM-dd
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastVisit;

    String phone;
}