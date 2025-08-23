package com.mail_campaign_system.fanout_worker.dto;

import java.util.UUID;

public record SendEmailEvent(
        UUID campaignId,
        int userId,
        String userEmail,
        String subject,
        String body
) {
}
