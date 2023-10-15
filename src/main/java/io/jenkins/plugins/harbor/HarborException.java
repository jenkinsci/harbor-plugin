package io.jenkins.plugins.harbor;

public class HarborException extends RuntimeException {
    public HarborException(String message) {
        super(message);
    }

    public HarborException(String message, Throwable cause) {
        super(message, cause);
    }

    public HarborException(Throwable cause) {
        super(cause);
    }
}
