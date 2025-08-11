package com.mail_campaign_system.announcement_service.service;

import com.mail_campaign_system.announcement_service.model.Announcement;

public interface EventService {

    void sendAnnouncementCreatedEvent(Announcement announcement);
}
