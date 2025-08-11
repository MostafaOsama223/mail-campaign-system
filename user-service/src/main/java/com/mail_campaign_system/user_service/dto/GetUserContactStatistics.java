package com.mail_campaign_system.user_service.dto;

public record GetUserContactStatistics(
        Integer totalContactsCount,
        Integer firstContactId
) {
}
