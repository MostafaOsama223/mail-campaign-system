package com.mail_campaing_system.trip_service.dto;

import java.sql.Timestamp;

public record TripCreateResponse(
        int id,
        String name,
        String description,
        Timestamp startDate,
        Timestamp endDate
) {
}
