package com.mail_campaign_system.user_service.service;

import com.mail_campaign_system.user_service.dto.GetUserContact;
import com.mail_campaign_system.user_service.dto.GetUserContactStatistics;
import com.mail_campaign_system.user_service.dto.GetUserContactsResponse;
import com.mail_campaign_system.user_service.repo.UserContactRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserContactService {

    private final UserContactRepo userContactRepo;

    public GetUserContactStatistics getUserContactsStatistics() {
        return userContactRepo.findStatistics();
    }

    public GetUserContactsResponse getUserContacts(int cursor, int limit) {
        List<GetUserContact> contacts = userContactRepo
                .findByIdGreaterThanOrderByIdAsc(cursor, limit)
                .stream()
                .map(contact -> new GetUserContact(
                        contact.getId(),
                        contact.getEmail()))
                .toList();

        Optional<Integer> nextId = contacts.isEmpty() ? Optional.empty() : Optional.of(contacts.get(contacts.size() - 1).id());

        log.info("Fetched {} user contacts starting from cursor {} with limit {}, nextId {}", contacts.size(), cursor, limit, nextId);
        return new GetUserContactsResponse(nextId, contacts);
    }
}
