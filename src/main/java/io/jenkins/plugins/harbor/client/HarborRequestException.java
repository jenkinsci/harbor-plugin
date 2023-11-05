package io.jenkins.plugins.harbor.client;

import java.io.IOException;

public class HarborRequestException extends IOException {
    private final int httpCode;

    public HarborRequestException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public HarborRequestException(int httpCode, String message, Throwable t) {
        super(message, t);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String toString() {
        return "HarborRequestException{" + "httpCode=" + httpCode + "," + "message=" + super.toString() + '}';
    }
}
