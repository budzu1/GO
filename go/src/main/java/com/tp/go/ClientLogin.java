package com.tp.go;

import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ClientLogin {

    private static final String SERVER_URL = "ws://localhost:8080/gs-guide-websocket";

    public void connect() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new LoginStompSessionHandler();
        try {
            StompSession stompSession = stompClient.connect(SERVER_URL, sessionHandler).get();

            Thread.sleep(20000);

            System.out.println("Press enter to exit");
            new Scanner(System.in).nextLine(); // Wait for user input to close the client
            stompSession.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
