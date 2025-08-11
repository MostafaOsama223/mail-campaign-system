package com.mail_campaign_system.announcement_service.dto;

import java.sql.Timestamp;

public record AnnouncementCreateRequest(
        String name,
        String description,
        Timestamp startDate,
        Timestamp endDate
) {
}
