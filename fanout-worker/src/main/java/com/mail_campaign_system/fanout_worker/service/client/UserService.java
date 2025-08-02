package com.mail_campaign_system.fanout_worker.service.client;

import com.mail_campaign_system.fanout_worker.dto.UserContact;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "/api/v1")
public interface UserService {

    @GetExchange(
            url = "/user-contacts?cursor={cursor}&limit={limit}",
            accept = MediaType.APPLICATION_JSON_VALUE)
    List<UserContact> getUserContacts(@RequestParam int cursor, @RequestParam int limit);
}
