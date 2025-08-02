package com.mail_campaign_system.fanout_worker.dto;

public record GetUserContact(
        Integer id,
        String email
) {
}
