package com.mail_campaign_system.announcement_service.service.implementation;

import com.mail_campaign_system.announcement_service.dto.AnnouncementCreateRequest;
import com.mail_campaign_system.announcement_service.dto.AnnouncementCreateResponse;
import com.mail_campaign_system.announcement_service.model.Announcement;
import com.mail_campaign_system.announcement_service.repo.AnnouncementRepo;
import com.mail_campaign_system.announcement_service.service.EventService;
import com.mail_campaign_system.announcement_service.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepo announcementRepo;
    private final EventService eventService;

    @Override
    public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest dto) {

        Announcement announcement = Announcement.builder()
                .name(dto.name())
                .description(dto.description())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();

        announcementRepo.saveAndFlush(announcement);

        eventService.sendAnnouncementCreatedEvent(announcement);

        return new AnnouncementCreateResponse(
                announcement.getId(),
                announcement.getName(),
                announcement.getDescription(),
                announcement.getStartDate(),
                announcement.getEndDate()
        );
    }
}
