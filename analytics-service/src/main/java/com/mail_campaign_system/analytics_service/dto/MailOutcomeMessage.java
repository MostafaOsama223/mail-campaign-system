package com.mail_campaign_system.analytics_service.dto;

import java.util.UUID;

public record MailOutcomeMessage(
    UUID campaignId,
    int userId,
    String userEmail,
    Statuses status,
    int latencyMs,
    int attemptNo
) {
}
