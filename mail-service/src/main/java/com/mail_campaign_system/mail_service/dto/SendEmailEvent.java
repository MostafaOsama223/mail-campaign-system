package com.mail_campaign_system.mail_service.dto;

public record SendEmailEvent(
        String email,
        String subject,
        String body
) {
}
