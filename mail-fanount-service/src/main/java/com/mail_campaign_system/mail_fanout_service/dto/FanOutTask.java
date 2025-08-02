package com.mail_campaign_system.mail_fanout_service.dto;

import java.util.UUID;

public record FanOutTask(
        UUID taskId,
        int tripId,
        int start_user_contact_id,
        int end_user_contact_id
) {
}
