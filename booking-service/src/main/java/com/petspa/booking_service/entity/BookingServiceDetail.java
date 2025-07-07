package com.petspa.booking_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "booking_service_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingServiceDetail {

    @Id
    private String id;

    private String serviceId;
    private String name;
    private BigDecimal price;
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @PrePersist
    public void ensureId() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
