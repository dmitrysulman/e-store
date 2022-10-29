package org.dmitrysulman.innopolis.diplomaproject.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    protected String message;
    protected Instant timestamp;
    protected int status;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, Instant timestamp, int status) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
