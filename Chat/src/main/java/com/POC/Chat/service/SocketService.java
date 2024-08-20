package com.POC.Chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.POC.Chat.model.User;
import com.POC.Chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    @Autowired
    private UserRepository userRepository;

    // Configurez la connexion et déconnexion des utilisateurs
    public void setupListeners(SocketIOServer server) {
        server.addConnectListener(client -> {
            String username = (String) client.getHandshakeData().getSingleUrlParam("username");
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setOnline(true);
                userRepository.save(user);
            }
        });

        server.addDisconnectListener(client -> {
            String username = (String) client.getHandshakeData().getSingleUrlParam("username");
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setOnline(false);
                userRepository.save(user);
            }
        });
    }
}
