package com.tp.go;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientConnectionService {

    private final HashMap<String, String> connectedClients = new HashMap<>();

    public void addConnection(String sessionId, String username) {
        connectedClients.put(sessionId, username);
    }

    public void removeConnection(String sessionId) {
        connectedClients.remove(sessionId);
    }

    public Map<String, String> getConnectedClients() {
        return new ConcurrentHashMap<>(connectedClients);
    }

    public Boolean ifConnected(String login) {
        return connectedClients.containsKey(login);
    }
}
