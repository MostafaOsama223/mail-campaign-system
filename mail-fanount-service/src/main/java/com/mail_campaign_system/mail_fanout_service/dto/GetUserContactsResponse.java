package com.mail_campaign_system.mail_fanout_service.dto;

import java.util.List;
import java.util.Optional;

public record GetUserContactsResponse(
        Optional<Integer> nextId,
        List<GetUserContact> contacts
) {
}
