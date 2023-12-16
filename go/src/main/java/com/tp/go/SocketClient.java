package com.tp.go;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

public class SocketClient extends StompSessionHandlerAdapter {

    private StompSession session;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected to server via WebSocket");
        // Perform your communication with the server here
        sendHandshake();
    }

    // ... Other methods for handling messages, errors, etc.

    public void sendHandshake() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String login = scanner.nextLine();

        // Customize your JSON payload creation as needed
        String jsonPayload = "{\"login\": \"" + login + "\"}";

        // Send the JSON message to the server
        session.send("/app/handshake", jsonPayload.getBytes());
    }

    public static void main(String[] args) {
        String serverEndpoint = "ws://localhost:8080/game";
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        SocketClient client = new SocketClient();

        stompClient.connect(serverEndpoint, client);
    }
}
