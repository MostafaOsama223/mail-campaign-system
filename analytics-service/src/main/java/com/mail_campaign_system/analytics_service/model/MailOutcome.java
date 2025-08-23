package com.mail_campaign_system.analytics_service.model;

import com.mail_campaign_system.analytics_service.dto.MailOutcomeMessage;
import com.mail_campaign_system.analytics_service.dto.Statuses;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mail_outcomes", schema = "analytics_service")
public class MailOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UUID campaignId;

    private int userId;

    private String userEmail;

    private int attemptNo;

    @Enumerated(EnumType.STRING)
    private Statuses status;

    private int latencyMs;

    @CreatedDate
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public static MailOutcome from(MailOutcomeMessage message) {
        return MailOutcome.builder()
                .campaignId(message.campaignId())
                .userId(message.userId())
                .userEmail(message.userEmail())
                .attemptNo(message.attemptNo())
                .status(message.status())
                .latencyMs(message.latencyMs())
                .createdAt(LocalDateTime.now())
                .build();
    }
}