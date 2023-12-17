package com.tp.go;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Scanner;

public class WebSocketConsoleClient {

    private static final String SERVER_URL = "ws://localhost:8080/gs-guide-websocket";

    public static void main(String[] args) {
        WebSocketConsoleClient client = new WebSocketConsoleClient();
        client.connect();
    }

    public void connect() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        try {
            StompSession stompSession = stompClient.connect(SERVER_URL, sessionHandler).get();

            // Read name from the keyboard
            System.out.print("Enter your name: ");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();

            // Send name message
            stompSession.send("/app/hello", name);

            System.out.println("Press enter to exit");
            new Scanner(System.in).nextLine(); // Wait for user input to close the client
            stompSession.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Greeting.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            Greeting greeting = (Greeting) payload;
            System.out.println("Received greeting: " + greeting.getContent());
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Connected to the server");

            // Subscribe to the "/topic/greetings" destination
            session.subscribe("/topic/greetings", this);
        }
    }
}
