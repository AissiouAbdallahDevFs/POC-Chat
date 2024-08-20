package com.POC.Chat.service;

import com.POC.Chat.dto.Message;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.messages.*;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SocketIOService {

    private final SocketIOServer server;

    // Map pour stocker les utilisateurs connectés
    private Map<String, String> connectedUsers = new HashMap<>();

    @Autowired
    public SocketIOService(SocketIOServer server) {
        this.server = server;
    }

    @PostConstruct
    private void startServer() {
        server.start();
    }

    @PreDestroy
    private void stopServer() {
        server.stop();
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        connectedUsers.put(client.getSessionId().toString(), userId);
        // Notifier les autres utilisateurs de la nouvelle connexion
        server.getBroadcastOperations().sendEvent("userConnected", userId);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String userId = connectedUsers.remove(client.getSessionId().toString());
        // Notifier les autres utilisateurs de la déconnexion
        server.getBroadcastOperations().sendEvent("userDisconnected", userId);
    }

    @OnEvent("privateMessage")
    public void onPrivateMessage(SocketIOClient client, AckRequest ackRequest, Message message) {
        // Logique pour envoyer un message privé
        String recipientSessionId = message.getRecipientId();
        server.getClient(UUID.fromString(recipientSessionId)).sendEvent("privateMessage", message);
    }

    public Map<String, String> getConnectedUsers() {
        return connectedUsers;
    }
}
