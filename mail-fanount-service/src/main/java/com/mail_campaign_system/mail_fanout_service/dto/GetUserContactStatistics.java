package com.mail_campaign_system.mail_fanout_service.dto;

public record GetUserContactStatistics(
        Integer totalContactsCount,
        Integer firstContactId
) {
}
