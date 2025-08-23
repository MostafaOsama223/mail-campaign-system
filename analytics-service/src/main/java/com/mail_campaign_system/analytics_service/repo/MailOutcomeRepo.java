package com.mail_campaign_system.analytics_service.repo;

import com.mail_campaign_system.analytics_service.model.MailOutcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailOutcomeRepo extends JpaRepository<MailOutcome, Integer> {
}
