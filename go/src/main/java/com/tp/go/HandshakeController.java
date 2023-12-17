package com.tp.go;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class HandshakeController {

    @Autowired
    private ClientConnectionService clientConnectionService;

    @MessageMapping("/login")
    @SendTo("/topic/login")
    public HandshakeReturnMessage handleHandshake(Handshakemessage message, SimpMessageHeaderAccessor headerAccessor) {

        HandshakeReturnMessage temp = new HandshakeReturnMessage();

        if (clientConnectionService.ifConnected(HtmlUtils.htmlEscape(message.getLogin()))) {
            temp.setStatus("error");
            temp.setMessage("User with given login is already connected!");
            System.out.println("bad login");
        } else {
            temp.setStatus("true");
            temp.setMessage("Succesfully conected!");
            System.out.println("good login");
        }

        return temp;
    }
}
