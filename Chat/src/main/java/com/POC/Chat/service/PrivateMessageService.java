package com.POC.Chat.service;

import com.POC.Chat.model.PrivateMessage;
import com.POC.Chat.model.User;
import com.POC.Chat.repository.PrivateMessageRepository;
import com.POC.Chat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;
    private final UserRepository userRepository;

    public PrivateMessageService(PrivateMessageRepository privateMessageRepository, UserRepository userRepository) {
        this.privateMessageRepository = privateMessageRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<PrivateMessage> getMessagesBetweenUsers(Long senderId, Long recipientId) {
        return privateMessageRepository.findMessagesBetweenUsers(senderId, recipientId);
    }

    @Transactional
    public PrivateMessage sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipient ID"));

        PrivateMessage message = new PrivateMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return privateMessageRepository.save(message);
    }
}
