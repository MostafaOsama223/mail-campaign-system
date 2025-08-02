package com.mail_campaign_system.fanout_worker.dto;

import java.util.List;
import java.util.Optional;

public record GetUserContactsResponse(
        Optional<Integer> nextId,
        List<GetUserContact> contacts
) {
}
