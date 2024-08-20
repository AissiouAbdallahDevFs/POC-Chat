package com.POC.Chat.controller;

import com.POC.Chat.model.PrivateMessage;
import com.POC.Chat.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private-messages")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    // Endpoint pour envoyer un message privé
    @PostMapping("/send")
    public ResponseEntity<PrivateMessage> sendMessage(@RequestParam Long senderId,
                                                       @RequestParam Long recipientId,
                                                       @RequestParam String content) {
        PrivateMessage message = privateMessageService.sendMessage(senderId, recipientId, content);
        return ResponseEntity.ok(message);
    }

    // Endpoint pour récupérer les messages entre deux utilisateurs
    @GetMapping("/between")
    public ResponseEntity<List<PrivateMessage>> getMessages(@RequestParam Long userId1,
                                                             @RequestParam Long userId2) {
        List<PrivateMessage> messages = privateMessageService.getMessages(userId1, userId2);
        return ResponseEntity.ok(messages);
    }
}
