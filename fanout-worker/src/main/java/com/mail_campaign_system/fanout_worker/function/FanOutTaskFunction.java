package com.mail_campaign_system.fanout_worker.function;

import com.mail_campaign_system.fanout_worker.dto.FanOutTask;
import com.mail_campaign_system.fanout_worker.dto.SendEmailEvent;
import com.mail_campaign_system.fanout_worker.dto.UserContact;
import com.mail_campaign_system.fanout_worker.service.client.UserService;
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
                .baseUrl("http://user-service/api/v1")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserService.class);
    }

    @Bean
    public Function<Message<FanOutTask>, List<Message<SendEmailEvent>>> fanOutTaskConsumer() {
        return fanOutTask -> {
            log.info("Processing FanOutTask: {}", fanOutTask.getPayload());

            FanOutTask payload = fanOutTask.getPayload();
            int cursor = payload.start_user_contact_id();
            int limit = payload.end_user_contact_id() - payload.start_user_contact_id() + 1;

//            TODO: Integrate with userService
            List<UserContact> userContacts = List.of(
                    new UserContact(1, "abc@gmail.com"),
                    new UserContact(2, "def@gmail.com"),
                    new UserContact(3, "ghi@gmail.com")
            );
            log.info("Fetched {} user contacts for FanOutTask: {}", userContacts.size(), fanOutTask.getPayload().taskId());

            List<Message<SendEmailEvent>> sendEmailEvents = userContacts.stream()
                    .map(userContact -> {
                        SendEmailEvent sendEmailEvent = new SendEmailEvent(
                                userContact.email(),
                                "Trip Notification",
                                "Dear User, you have a new trip notification."
                        );
                        return MessageBuilder.withPayload(sendEmailEvent).build();
                    }).toList();
            log.info("Created {} SendEmailEvents for FanOutTask: {}", sendEmailEvents.size(), fanOutTask.getPayload().taskId());

            return sendEmailEvents;
        };
    }

}
