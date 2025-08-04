package com.mail_campaign_system.announcement_service.dto;

import java.sql.Timestamp;

public record AnnouncementCreateResponse(
        int id,
        String name,
        String description,
        Timestamp startDate,
        Timestamp endDate
) {
}
