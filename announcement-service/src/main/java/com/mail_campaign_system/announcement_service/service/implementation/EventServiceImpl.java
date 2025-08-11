package com.mail_campaign_system.announcement_service.service.implementation;

import com.mail_campaign_system.announcement_service.dto.AnnouncementCreatedEvent;
import com.mail_campaign_system.announcement_service.model.Announcement;
import com.mail_campaign_system.announcement_service.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Value("${spring.application.name}")
    String applicationName;

    private final StreamBridge streamBridge;

    @Override
    public void sendAnnouncementCreatedEvent(Announcement announcement) {

        AnnouncementCreatedEvent event = new AnnouncementCreatedEvent(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                applicationName,
                announcement.getId(),
                announcement.getName()
        );

        log.info("Publishing AnnouncementCreatedEvent: {}", event);

        streamBridge.send("AnnouncementCreatedEvent-out-0", event);
    }
}
