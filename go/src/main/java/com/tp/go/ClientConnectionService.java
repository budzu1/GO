package com.tp.go;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientConnectionService implements ApplicationListener<SessionDisconnectEvent> {

    private final Map<String, String> connectedClients = new HashMap<>();

    public void addConnection(String sessionId, String username) {
        connectedClients.put(sessionId, username);
    }

    public void removeConnection(String sessionId) {
        connectedClients.remove(sessionId);
    }

    public Map<String, String> getConnectedClients() {
        return new ConcurrentHashMap<>(connectedClients);
    }

    public boolean ifConnected(String login) {
        return connectedClients.containsValue(login);
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        System.out.println("close detected");
        String sessionId = event.getSessionId();
        System.out.println("Session ID to remove: " + sessionId);
        removeConnection(sessionId);
    }
}
