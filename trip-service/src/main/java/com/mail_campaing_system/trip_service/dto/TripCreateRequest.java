package com.mail_campaing_system.trip_service.dto;

import java.sql.Timestamp;

public record TripCreateRequest(
        String name,
        String description,
        Timestamp startDate,
        Timestamp endDate
) {
}
