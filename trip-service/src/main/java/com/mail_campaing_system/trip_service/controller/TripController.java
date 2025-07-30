package com.mail_campaing_system.trip_service.controller;

import com.mail_campaing_system.trip_service.dto.TripCreateRequest;
import com.mail_campaing_system.trip_service.dto.TripCreateResponse;
import com.mail_campaing_system.trip_service.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripCreateRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(dto));
    }

}
