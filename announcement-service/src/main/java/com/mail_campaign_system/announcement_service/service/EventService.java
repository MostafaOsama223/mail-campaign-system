package com.mail_campaign_system.announcement_service.service;

import com.mail_campaign_system.announcement_service.model.Trip;

public interface EventService {

    void sendTripCreatedEvent(Trip trip);
}
