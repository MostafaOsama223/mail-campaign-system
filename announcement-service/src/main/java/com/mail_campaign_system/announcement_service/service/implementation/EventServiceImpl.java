package com.mail_campaign_system.announcement_service.service.implementation;

import com.mail_campaign_system.announcement_service.dto.TripCreatedEvent;
import com.mail_campaign_system.announcement_service.model.Trip;
import com.mail_campaign_system.announcement_service.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Value("${spring.application.name}")
    String applicationName;

    private final StreamBridge streamBridge;

    @Override
    public void sendTripCreatedEvent(Trip trip) {

        TripCreatedEvent event = new TripCreatedEvent(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                applicationName,
                trip.getId(),
                trip.getName()
        );

        log.info("Publishing TripCreatedEvent: {}", event);

        streamBridge.send("TripCreatedEvent-out-0", event);
    }

//    TODO: Remove this block
//    @Bean
//    Consumer<TripCreatedEvent> tripEventCreated() {
//        return event -> {
//            log.info("Received TripCreatedEvent: {}", event);
//        };
//    }
}
