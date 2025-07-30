package com.mail_campaing_system.trip_service.repo;

import com.mail_campaing_system.trip_service.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepo extends JpaRepository<Trip, Integer> {
}
