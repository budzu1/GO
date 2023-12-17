package com.tp.go;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class WebSocketClient {

    public static void main(String[] args) {
        String serverUrl = "ws://localhost:8080/go-game"; // Replace with your server URL

        WebSocketClient client = new WebSocketClient();
        client.connect(serverUrl);
    }

    public void connect(String serverUrl) {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        try {
            StompSession stompSession = stompClient.connect(serverUrl, sessionHandler).get();
            System.out.println("Press enter to exit");
            new Scanner(System.in).nextLine(); // Wait for user input to close the client
            stompSession.disconnect();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Connected to the server");

            // Send login message
            Handshakemessage handshakemessage = new Handshakemessage();
            handshakemessage.setLogin("xd"); // Replace with your desired username

            session.send("/app/login", handshakemessage);
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return HandshakeReturnMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            HandshakeReturnMessage response = (HandshakeReturnMessage) payload;
            System.out.println("Status: " + response.getStatus());
            System.out.println("Message: " + response.getMessage());
        }
    }
}
