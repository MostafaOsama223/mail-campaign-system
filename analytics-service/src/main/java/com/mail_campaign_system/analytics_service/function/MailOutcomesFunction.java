package com.mail_campaign_system.analytics_service.function;


import com.mail_campaign_system.analytics_service.dto.MailOutcomeMessage;
import com.mail_campaign_system.analytics_service.model.MailOutcome;
import com.mail_campaign_system.analytics_service.repo.MailOutcomeRepo;
import com.rabbitmq.stream.MessageHandler;
import com.rabbitmq.stream.OffsetSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

import java.util.function.Consumer;


@Slf4j
@Configuration
public class MailOutcomesFunction {

    @Bean
    public Consumer<Message<MailOutcomeMessage>> mailsOutcomes(MailOutcomeRepo mailOutcomeRepo) {
        return mailOutcomeMessage -> {
            log.info("Received message: {}", mailOutcomeMessage.getPayload());

            mailOutcomeRepo.save(MailOutcome.from(mailOutcomeMessage.getPayload()));

            MessageHandler.Context context = mailOutcomeMessage.getHeaders()
                    .get("rabbitmq_streamContext", MessageHandler.Context.class);
            context.consumer().store(context.offset());
        };
    }

    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> mailsOutcomesCustomizer() {
        return (cont, destination, group) -> {
            if (cont instanceof StreamListenerContainer container) {
                container.setConsumerCustomizer((name, builder) -> {
                    log.info("Setting up consumer for name: {}", name);
                    builder
                            .stream("mails_outcomes.analytics-service")
                            .manualTrackingStrategy()
                            .builder()
                            .offset(OffsetSpecification.first());
                });
            }
        };
    }
}
