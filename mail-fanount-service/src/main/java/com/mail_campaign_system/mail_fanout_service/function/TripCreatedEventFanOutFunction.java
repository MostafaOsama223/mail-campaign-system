package com.mail_campaign_system.mail_fanout_service.function;

import com.mail_campaign_system.mail_fanout_service.dto.FanOutTask;
import com.mail_campaign_system.mail_fanout_service.dto.TripCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Configuration
public class TripCreatedEventFanOutFunction {

    @Value("${mail.fanout.service.batch.size:1000}")
    private int batchSize;

    @Bean
    public Function<TripCreatedEvent, List<Message<FanOutTask>>> tripCreatedEventConsumer() {
        return tripCreatedEvent -> {

//            TODO: Integrate with UserService to fetch user contacts
            long totalNumberOfUsers = 6000;
            long numberOfBatches = (totalNumberOfUsers + batchSize - 1) / batchSize;

            log.info("TripCreatedEvent received {}. Batch size: {}", tripCreatedEvent, batchSize);

            List<Message<FanOutTask>> fanOutTasks = new ArrayList<>((int) numberOfBatches);

            for (int i = 0; i < numberOfBatches; i++) {
                FanOutTask fanOutTask = new FanOutTask(
                        UUID.randomUUID(),
                        tripCreatedEvent.tripId(),
                        i * batchSize,
                        Math.min((i + 1) * batchSize - 1, (int) totalNumberOfUsers - 1)
                );
                fanOutTasks.add(MessageBuilder.withPayload(fanOutTask).build());

                log.info("Created FanOutTask: {}", fanOutTask);
            }

            log.info("Total FanOutTasks created: {}", fanOutTasks.size());
            return fanOutTasks;
        };
    }
}

