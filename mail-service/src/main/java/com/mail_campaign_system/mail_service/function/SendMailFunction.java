package com.mail_campaign_system.mail_service.function;

import com.mail_campaign_system.mail_service.dto.MailOutcomeMessage;
import com.mail_campaign_system.mail_service.dto.SendEmailEvent;
import com.mail_campaign_system.mail_service.dto.Statuses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class SendMailFunction {

    @Value("${spring.cloud.stream.bindings.mailConsumer-in-0.destination}-${spring.cloud.stream.bindings.mailConsumer-in-0.group}")
    private String ORIGINAL_QUEUE;

    @Value("${app.mail.dlq-cycles-before-park}")
    private long DLQ_CYCLES_BEFORE_PARK;

//    TODO: Refactor this method
    @Bean
    public Consumer<Message<SendEmailEvent>> mailConsumer(StreamBridge streamBridge) {
        return message -> {
            SendEmailEvent sendEmailEvent = message.getPayload();

            log.info(
                    "---- Processing message from queue: {} ----\n"
                            + "Message Headers: {}\n"
                            + "Message Payload: {}",
                    ORIGINAL_QUEUE,
                    message.getHeaders(),
                    sendEmailEvent);

            if (sendEmailEvent.userEmail().equals("user-00b085fa-573b-4c3c-90b9-fd4b4188c19d@eg.com")) {

                long dlqCycles = extractDlqCycles(message.getHeaders());

                if (dlqCycles >= DLQ_CYCLES_BEFORE_PARK) {
                    log.info("---- Message {} has been retried {} times, sending to Parking Lot ----", message.getHeaders(), dlqCycles);

                    streamBridge.send(
                            "parkingLot-out-0",
                            MessageBuilder.withPayload(sendEmailEvent)
                                    .copyHeadersIfAbsent(message.getHeaders())
                                    .setHeader("original-queue", ORIGINAL_QUEUE)
                                    .setHeader("parking-reason", "Exceeded DLQ cycles")
                                    .build()
                    );

                    streamBridge.send(
                            "mailOutcome-out-0",
                            MessageBuilder.withPayload(
                                            new MailOutcomeMessage(
                                                    sendEmailEvent.campaignId(),
                                                    sendEmailEvent.userId(),
                                                    sendEmailEvent.userEmail(),
                                                    Statuses.FAILED,
                                                    0, // latencyMs is not calculated here
                                                    (int) dlqCycles + 1
                                            )
                                    )
                                    .copyHeadersIfAbsent(message.getHeaders())
                                    .build()
                    );

                    throw new ImmediateAcknowledgeAmqpException("Parked after all DLQ cycles");
                }

                throw new AmqpRejectAndDontRequeueException("Simulated failure for testing purposes, message will be sent to DLQ");
            }

            streamBridge.send(
                    "mailOutcome-out-0",
                    MessageBuilder.withPayload(
                                    new MailOutcomeMessage(
                                            sendEmailEvent.campaignId(),
                                            sendEmailEvent.userId(),
                                            sendEmailEvent.userEmail(),
                                            Statuses.SUCCESS,
                                            0, // latencyMs is not calculated here
                                            1 // attemptNo is set to 1 for successful sends
                                    )
                            )
                            .copyHeadersIfAbsent(message.getHeaders())
                            .build()
            );
        };
    }

    private long extractDlqCycles(MessageHeaders headers) {
        Object xDeath = headers.get("x-death");
        if (xDeath instanceof List<?> list && !list.isEmpty()) {
            Object firstElement = list.get(0);
            if (firstElement instanceof Map<?, ?> map) {
                Object count = map.get("count");

                if (count instanceof Number number) return number.longValue();
            }
        }
        return 0L;
    }
}
