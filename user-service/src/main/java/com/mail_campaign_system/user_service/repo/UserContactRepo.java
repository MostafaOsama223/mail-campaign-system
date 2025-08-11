package com.mail_campaign_system.user_service.repo;

import com.mail_campaign_system.user_service.dto.GetUserContactStatistics;
import com.mail_campaign_system.user_service.model.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserContactRepo extends JpaRepository<UserContact, Long> {

    @Query("SELECT COUNT(u) AS totalContactsCount, MIN(u.id) AS firstContactId FROM UserContact u")
    GetUserContactStatistics findStatistics();

    @Query("SELECT u FROM UserContact u WHERE u.id > :cursor ORDER BY u.id ASC LIMIT :limit")
    List<UserContact> findByIdGreaterThanOrderByIdAsc(int cursor, int limit);
}
