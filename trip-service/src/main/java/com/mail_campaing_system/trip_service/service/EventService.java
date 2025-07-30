package com.mail_campaing_system.trip_service.service;

import com.mail_campaing_system.trip_service.model.Trip;

public interface EventService {

    void sendTripCreatedEvent(Trip trip);
}
