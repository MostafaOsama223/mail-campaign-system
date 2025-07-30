package com.mail_campaing_system.trip_service.service;

import com.mail_campaing_system.trip_service.dto.TripCreateRequest;
import com.mail_campaing_system.trip_service.dto.TripCreateResponse;

public interface TripService {

    TripCreateResponse createTrip(TripCreateRequest dto);
}
