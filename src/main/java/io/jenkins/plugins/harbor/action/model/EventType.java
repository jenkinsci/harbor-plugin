package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * WebHook Event Type
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/event/topic.go">EventType</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum EventType {
    CREATE_PROJECT("CREATE_PROJECT"),
    DELETE_PROJECT("DELETE_PROJECT"),
    PUSH_ARTIFACT("PUSH_ARTIFACT"),
    PULL_ARTIFACT("PULL_ARTIFACT"),
    DELETE_ARTIFACT("DELETE_ARTIFACT"),
    DELETE_REPOSITORY("DELETE_REPOSITORY"),
    CREATE_TAG("CREATE_TAG"),
    DELETE_TAG("DELETE_TAG"),
    SCANNING_FAILED("SCANNING_FAILED"),
    SCANNING_STOPPED("SCANNING_STOPPED"),
    SCANNING_COMPLETED("SCANNING_COMPLETED");

    @JsonValue
    private final String eventType;

    EventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
