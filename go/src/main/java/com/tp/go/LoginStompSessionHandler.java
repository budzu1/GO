package com.tp.go;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.Scanner;

public class LoginStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return HandshakeReturnMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        HandshakeReturnMessage response = (HandshakeReturnMessage) payload;
        System.out.println("Received greeting: " + response.getMessage());
        if ("error".equals(response.getStatus())) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected to the server");
        System.out.print("Enter your login: ");
        Scanner scanner = new Scanner(System.in);
        String login = scanner.nextLine();

        session.subscribe("/user/" + login + "/topic/loginResponse", this);

        session.send("/app/login", login);
    }

}
