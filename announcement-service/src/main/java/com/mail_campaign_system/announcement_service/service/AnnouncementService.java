package com.mail_campaign_system.announcement_service.service;

import com.mail_campaign_system.announcement_service.dto.AnnouncementCreateRequest;
import com.mail_campaign_system.announcement_service.dto.AnnouncementCreateResponse;

public interface AnnouncementService {

    AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest dto);
}
