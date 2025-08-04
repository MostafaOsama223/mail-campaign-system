package com.mail_campaign_system.announcement_service.service;

import com.mail_campaign_system.announcement_service.dto.TripCreateRequest;
import com.mail_campaign_system.announcement_service.dto.TripCreateResponse;

public interface TripService {

    TripCreateResponse createTrip(TripCreateRequest dto);
}
