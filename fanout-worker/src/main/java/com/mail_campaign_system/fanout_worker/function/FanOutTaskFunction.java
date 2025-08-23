package com.mail_campaign_system.fanout_worker.function;

import com.mail_campaign_system.fanout_worker.dto.FanOutTask;
import com.mail_campaign_system.fanout_worker.dto.GetUserContact;
import com.mail_campaign_system.fanout_worker.dto.SendEmailEvent;
import com.mail_campaign_system.fanout_worker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Configuration
public class FanOutTaskFunction {

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
    public Function<Message<FanOutTask>, List<Message<SendEmailEvent>>> fanOutTaskConsumer(UserService userService) {
        return fanOutTask -> {
            log.info("Processing FanOutTask: {}", fanOutTask.getPayload());

            FanOutTask payload = fanOutTask.getPayload();
            int cursor = payload.start_user_contact_id();
            int limit = payload.end_user_contact_id() - payload.start_user_contact_id() + 1;

            List<GetUserContact> userContacts = userService.getUserContacts(cursor, limit).contacts();

            List<Message<SendEmailEvent>> sendEmailEvents = userContacts.stream()
                    .map(userContact -> {
                        log.debug("Creating SendEmailEvent for user contact: {}", userContact);
                        SendEmailEvent sendEmailEvent = new SendEmailEvent(
                                payload.taskId(),
                                userContact.id(),
                                userContact.email(),
                                "Trip Notification",
                                "Dear User, you have a new trip notification."
                        );

                        String partitionKey = payload.tripId() + "-" + userContact.id();
                        return MessageBuilder
                                .withPayload(sendEmailEvent)
                                .setHeader("partitionKey", partitionKey)
                                .build();
                    }).toList();
            log.info("Created {} SendEmailEvents for FanOutTask: {}", sendEmailEvents.size(), payload.taskId());

            return sendEmailEvents;
        };
    }

}
