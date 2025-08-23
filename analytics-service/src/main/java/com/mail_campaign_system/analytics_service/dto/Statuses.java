package com.mail_campaign_system.analytics_service.dto;

public enum Statuses {
    SUCCESS("success"),
    FAILED("failed"),
    RETRY("retry");

    private final String status;

    Statuses(String status) {
        this.status = status;
    }
}
