package com.mail_campaing_system.trip_service.dto;

import java.util.UUID;

public record TripCreatedEvent(
        UUID eventId,
        long eventTime,
        String trace,
        int tripId,
        String tripName

) {
}
