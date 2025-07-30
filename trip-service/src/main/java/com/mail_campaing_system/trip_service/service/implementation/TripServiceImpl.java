package com.mail_campaing_system.trip_service.service.implementation;

import com.mail_campaing_system.trip_service.dto.TripCreateRequest;
import com.mail_campaing_system.trip_service.dto.TripCreateResponse;
import com.mail_campaing_system.trip_service.model.Trip;
import com.mail_campaing_system.trip_service.repo.TripRepo;
import com.mail_campaing_system.trip_service.service.EventService;
import com.mail_campaing_system.trip_service.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepo tripRepo;
    private final EventService eventService;

    @Override
    public TripCreateResponse createTrip(TripCreateRequest dto) {

        Trip trip = Trip.builder()
                .name(dto.name())
                .description(dto.description())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();

        tripRepo.saveAndFlush(trip);

        eventService.sendTripCreatedEvent(trip);

        return new TripCreateResponse(
                trip.getId(),
                trip.getName(),
                trip.getDescription(),
                trip.getStartDate(),
                trip.getEndDate()
        );
    }
}
