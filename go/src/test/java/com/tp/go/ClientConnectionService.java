package com.tp.go;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class ClientConnectionServiceTest {

    @InjectMocks
    private ClientConnectionService clientConnectionService;

    @Mock
    private StompHeaderAccessor headerAccessor;

    @Mock
    private Message<byte[]> message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addConnectionShouldAddConnection() {
        // Arrange
        String sessionId = "sessionId";
        String username = "testUser";

        // Act
        clientConnectionService.addConnection(sessionId, username);

        // Assert
        assert (clientConnectionService.getConnectedClients().containsKey(sessionId));
        assert (clientConnectionService.getConnectedClients().containsValue(username));
    }

    @Test
    void removeConnectionShouldRemoveConnection() {
        // Arrange
        String sessionId = "sessionId";
        clientConnectionService.addConnection(sessionId, "testUser");

        // Act
        clientConnectionService.removeConnection(sessionId);

        // Assert
        assert (!clientConnectionService.getConnectedClients().containsKey(sessionId));
    }

    @Test
    void onApplicationEventShouldRemoveConnection() {
        // Arrange
        String sessionId = "000000";
        String username = "user";
        clientConnectionService.addConnection(sessionId, username);

        when(headerAccessor.getSessionId()).thenReturn(sessionId);

        // Print the state before removal
        System.out.println("Before removal: " + clientConnectionService.getConnectedClients());

        // Act
        clientConnectionService
                .onApplicationEvent(new SessionDisconnectEvent(this, message, sessionId, CloseStatus.NORMAL));

        // Print the state after removal
        System.out.println("After removal: " + clientConnectionService.getConnectedClients());

        // Assert
        assertFalse(clientConnectionService.ifConnected(username));
    }
}
