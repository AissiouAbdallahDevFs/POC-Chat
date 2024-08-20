package com.POC.Chat.repository;

import com.POC.Chat.model.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    @Query("SELECT pm FROM PrivateMessage pm WHERE " +
           "(pm.sender.id = :userId1 AND pm.recipient.id = :userId2) OR " +
           "(pm.sender.id = :userId2 AND pm.recipient.id = :userId1) " +
           "ORDER BY pm.timestamp ASC")
    List<PrivateMessage> findMessagesBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    
}
