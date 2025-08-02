package com.mail_campaign_system.fanout_worker.dto;

public record SendEmailEvent(
        String email,
        String subject,
        String body
) {
}
