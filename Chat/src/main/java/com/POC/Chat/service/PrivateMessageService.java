package com.POC.Chat.service;

import com.POC.Chat.model.PrivateMessage;
import com.POC.Chat.model.User;
import com.POC.Chat.repository.PrivateMessageRepository;
import com.POC.Chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrivateMessageService {

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    @Autowired
    private UserRepository userRepository;

    // Méthode pour envoyer un message privé
    public PrivateMessage sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found"));

        PrivateMessage message = new PrivateMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return privateMessageRepository.save(message);
    }

    // Méthode pour obtenir tous les messages entre deux utilisateurs
    public List<PrivateMessage> getMessages(Long userId1, Long userId2) {
        return privateMessageRepository.findMessagesBetweenUsers(userId1, userId2);
    }
}
