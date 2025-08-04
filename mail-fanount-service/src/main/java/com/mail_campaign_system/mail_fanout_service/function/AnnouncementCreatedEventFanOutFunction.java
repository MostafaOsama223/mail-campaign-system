package com.mail_campaign_system.mail_fanout_service.function;

import com.mail_campaign_system.mail_fanout_service.dto.FanOutTask;
import com.mail_campaign_system.mail_fanout_service.dto.GetUserContactStatistics;
import com.mail_campaign_system.mail_fanout_service.dto.AnnouncementCreatedEvent;
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
public class AnnouncementCreatedEventFanOutFunction {

    @Value("${mail.fanout.service.batch.size:1000}")
    private int batchSize;

    @Value("${userservice.url:http://user-service:8095/api/v1}")
    private String userServiceBaseUrl;

    @Bean
    public UserService userService() {
        RestClient restClient = RestClient.builder()
                .baseUrl(userServiceBaseUrl)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserService.class);
    }

    @Bean
    public Function<AnnouncementCreatedEvent, List<Message<FanOutTask>>> announcementCreatedEventConsumer(UserService userService) {
        return announcementCreatedEvent -> {

            GetUserContactStatistics userContactStatistics = userService.getUserContactStatistics();

            int totalNumberOfUsers = userContactStatistics.totalContactsCount();
            int firstContactId = userContactStatistics.firstContactId();
            int numberOfBatches = (totalNumberOfUsers + batchSize - 1) / batchSize;

            log.info("AnnouncementCreatedEvent received {}. Batch size: {}", announcementCreatedEvent, batchSize);

            List<Message<FanOutTask>> fanOutTasks = new ArrayList<>(numberOfBatches);

            for (int i = 0; i < numberOfBatches; i++) {
                FanOutTask fanOutTask = new FanOutTask(
                        UUID.randomUUID(),
                        announcementCreatedEvent.announcementId(),
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

