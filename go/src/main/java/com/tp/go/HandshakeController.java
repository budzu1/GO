package com.tp.go;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal; // Import the Principal class

@Controller
public class HandshakeController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ClientConnectionService clientConnectionService;

    @MessageMapping("/login")
    public void handleHandshake(Handshakemessage message, SimpMessageHeaderAccessor headerAccessor) {

        HandshakeReturnMessage temp = new HandshakeReturnMessage();

        String login = HtmlUtils.htmlEscape(message.getLogin());

        String sessionId = headerAccessor.getSessionId();
        System.out.println(headerAccessor.getUser());
        System.out.println(headerAccessor.getSessionId());

        if (clientConnectionService.ifConnected(login)) {
            temp.setStatus("error");
            temp.setMessage("User with given login is already connected!");
            System.out.println("bad login");
        } else {
            temp.setStatus("success");
            temp.setMessage("Successfully connected!");
            System.out.println("good login");
            clientConnectionService.addConnection(sessionId, login);
        }

        messagingTemplate.convertAndSendToUser(login, "/topic/loginResponse", temp);
        System.out.println("return handshake has been sent");

    }
}
