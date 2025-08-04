package com.mail_campaign_system.announcement_service.controller;

import com.mail_campaign_system.announcement_service.dto.AnnouncementCreateRequest;
import com.mail_campaign_system.announcement_service.dto.AnnouncementCreateResponse;
import com.mail_campaign_system.announcement_service.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementCreateResponse> createAnnouncement(@RequestBody AnnouncementCreateRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(announcementService.createAnnouncement(dto));
    }

}
