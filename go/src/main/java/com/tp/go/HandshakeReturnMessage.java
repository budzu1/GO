package com.tp.go;

public class HandshakeReturnMessage {

    private String status;
    private String message;

    public HandshakeReturnMessage() {

    }

    public HandshakeReturnMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
