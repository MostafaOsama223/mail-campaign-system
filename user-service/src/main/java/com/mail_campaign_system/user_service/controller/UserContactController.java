package com.mail_campaign_system.user_service.controller;

import com.mail_campaign_system.user_service.dto.GetUserContactStatistics;
import com.mail_campaign_system.user_service.dto.GetUserContactsResponse;
import com.mail_campaign_system.user_service.service.UserContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-contacts")
public class UserContactController {

    private final UserContactService userContactService;

    @GetMapping("/statistics")
    public GetUserContactStatistics getUserContactsStatistics() {
        return userContactService.getUserContactsStatistics();
    }

    @GetMapping
    public GetUserContactsResponse getUserContacts(@RequestParam int cursor, @RequestParam int limit) {
        return userContactService.getUserContacts(cursor, limit);
    }
}
