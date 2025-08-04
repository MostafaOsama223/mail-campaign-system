package com.mail_campaign_system.announcement_service.repo;

import com.mail_campaign_system.announcement_service.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepo extends JpaRepository<Announcement, Integer> {
}
