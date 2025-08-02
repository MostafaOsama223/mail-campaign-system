package com.mail_campaign_system.mail_fanout_service.function;

import com.mail_campaign_system.mail_fanout_service.dto.FanOutTask;
import com.mail_campaign_system.mail_fanout_service.dto.GetUserContactStatistics;
import com.mail_campaign_system.mail_fanout_service.dto.TripCreatedEvent;
import com.mail_campaign_system.mail_fanout_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

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
    public UserService userService() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8095/api/v1")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserService.class);
    }

    @Bean
    public Function<TripCreatedEvent, List<Message<FanOutTask>>> tripCreatedEventConsumer(UserService userService) {
        return tripCreatedEvent -> {

            GetUserContactStatistics userContactStatistics = userService.getUserContactStatistics();

            int totalNumberOfUsers = userContactStatistics.totalContactsCount();
            int firstContactId = userContactStatistics.firstContactId();
            int numberOfBatches = (totalNumberOfUsers + batchSize - 1) / batchSize;

            log.info("TripCreatedEvent received {}. Batch size: {}", tripCreatedEvent, batchSize);

            List<Message<FanOutTask>> fanOutTasks = new ArrayList<>(numberOfBatches);

            for (int i = 0; i < numberOfBatches; i++) {
                FanOutTask fanOutTask = new FanOutTask(
                        UUID.randomUUID(),
                        tripCreatedEvent.tripId(),
                        i * batchSize + firstContactId,
                        Math.min((i + 1) * batchSize + firstContactId - 1, totalNumberOfUsers - 1)
                );
                fanOutTasks.add(MessageBuilder.withPayload(fanOutTask).build());

                log.info("Created FanOutTask: {}", fanOutTask);
            }

            log.info("Total FanOutTasks created: {}", fanOutTasks.size());
            return fanOutTasks;
        };
    }
}

