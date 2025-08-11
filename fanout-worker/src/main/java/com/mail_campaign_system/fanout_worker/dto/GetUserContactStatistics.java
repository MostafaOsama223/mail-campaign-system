package com.mail_campaign_system.fanout_worker.dto;

public record GetUserContactStatistics(
        Integer totalContactsCount,
        Integer firstContactId
) {
}
