package com.mail_campaign_system.mail_fanout_service.dto;

import java.util.UUID;

public record TripCreatedEvent(
        UUID eventId,
        long eventTime,
        String trace,
        int tripId,
        String tripName

) {
}
