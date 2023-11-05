package io.jenkins.plugins.harbor.steps;

import io.jenkins.plugins.harbor.HarborException;

public class ImageInfoExtractionException extends HarborException {
    public ImageInfoExtractionException(String message) {
        super(message);
    }

    public ImageInfoExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageInfoExtractionException(Throwable cause) {
        super(cause);
    }
}
