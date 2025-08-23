package com.mail_campaign_system.mail_service.dto;

public enum Statuses {
    SUCCESS("success"),
    FAILED("failed"),
    RETRY("retry");

    private final String status;

    Statuses(String status) {
        this.status = status;
    }
}
