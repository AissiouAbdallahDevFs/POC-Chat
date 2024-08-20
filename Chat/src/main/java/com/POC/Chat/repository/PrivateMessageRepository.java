package com.POC.Chat.repository;

import com.POC.Chat.model.PrivateMessage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

     @Query("SELECT pm FROM PrivateMessage pm WHERE pm.sender.id = :senderId AND pm.recipient.id = :recipientId")
    PrivateMessage findBySenderIdAndRecipientId(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId);

     // Trouver les messages entre deux utilisateurs
    List<PrivateMessage> findMessagesBySenderIdAndRecipientId(Long senderId, Long recipientId);

     // Trouver les messages entre deux utilisateurs, en tenant compte de l'ordre
     default List<PrivateMessage> findMessagesBetweenUsers(Long userId1, Long userId2) {
         return findMessagesBySenderIdAndRecipientId(userId1, userId2);
     }

     List<PrivateMessage> findByRecipientId(Long recipientId);
}
