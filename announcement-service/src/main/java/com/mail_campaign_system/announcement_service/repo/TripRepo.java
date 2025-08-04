package com.mail_campaign_system.announcement_service.repo;

import com.mail_campaign_system.announcement_service.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepo extends JpaRepository<Trip, Integer> {
}
