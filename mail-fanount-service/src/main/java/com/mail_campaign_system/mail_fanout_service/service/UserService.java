package com.mail_campaign_system.mail_fanout_service.service;

import com.mail_campaign_system.mail_fanout_service.dto.GetUserContactStatistics;
import com.mail_campaign_system.mail_fanout_service.dto.GetUserContactsResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "http://user-service:8095/api/v1")
public interface UserService {

    @GetExchange("/user-contacts/statistics")
    GetUserContactStatistics getUserContactStatistics();

    @GetExchange("/user-contacts")
    GetUserContactsResponse getUserContacts(@RequestParam int cursor, @RequestParam int limit);
}
