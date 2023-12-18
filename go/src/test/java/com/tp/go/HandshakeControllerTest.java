package com.tp.go;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HandshakeControllerTest {

    @InjectMocks
    private HandshakeController handshakeController;

    @Mock
    private SimpMessageSendingOperations messagingTemplate;

    @Mock
    private ClientConnectionService clientConnectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleHandshakeSuccess() {
        // Arrange
        Handshakemessage message = new Handshakemessage("testUser");
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        when(headerAccessor.getSessionId()).thenReturn("sessionId");
        when(clientConnectionService.ifConnected("testUser")).thenReturn(false);

        // Act
        handshakeController.handleHandshake(message, headerAccessor);

        // Assert
        verify(clientConnectionService).addConnection("sessionId", "testUser");

        // Capture the arguments passed to convertAndSendToUser
        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HandshakeReturnMessage> messageCaptor = ArgumentCaptor.forClass(HandshakeReturnMessage.class);

        verify(messagingTemplate).convertAndSendToUser(
                userCaptor.capture(),
                destinationCaptor.capture(),
                messageCaptor.capture());

        // Assert specific values
        assertEquals("testUser", userCaptor.getValue());
        assertEquals("/topic/loginResponse", destinationCaptor.getValue());

        // Check specific properties of the HandshakeReturnMessage
        HandshakeReturnMessage capturedMessage = messageCaptor.getValue();
        assertEquals("success", capturedMessage.getStatus());
        assertEquals("Successfully connected!", capturedMessage.getMessage());
    }

    @Test
    void testHandleHandshakeError() {
        // Arrange
        Handshakemessage message = new Handshakemessage("existingUser");
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        when(headerAccessor.getSessionId()).thenReturn("existingSessionId");
        when(clientConnectionService.ifConnected("existingUser")).thenReturn(true);

        // Act
        handshakeController.handleHandshake(message, headerAccessor);

        // Assert
        verify(clientConnectionService, never()).addConnection(anyString(), anyString());

        // Capture the arguments passed to convertAndSendToUser
        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HandshakeReturnMessage> messageCaptor = ArgumentCaptor.forClass(HandshakeReturnMessage.class);

        verify(messagingTemplate).convertAndSendToUser(
                userCaptor.capture(),
                destinationCaptor.capture(),
                messageCaptor.capture());

        // Assert specific values
        assertEquals("existingUser", userCaptor.getValue());
        assertEquals("/topic/loginResponse", destinationCaptor.getValue());

        // Check specific properties of the HandshakeReturnMessage
        HandshakeReturnMessage capturedMessage = messageCaptor.getValue();
        assertEquals("error", capturedMessage.getStatus());
        assertEquals("User with given login is already connected!", capturedMessage.getMessage());
    }

}
