package com.POC.Chat.repository;

import com.POC.Chat.model.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    @Query("SELECT pm FROM PrivateMessage pm WHERE (pm.sender.id = :senderId AND pm.recipient.id = :recipientId) " +
            "OR (pm.sender.id = :recipientId AND pm.recipient.id = :senderId) ORDER BY pm.timestamp ASC")
    List<PrivateMessage> findMessagesBetweenUsers(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId);
}
