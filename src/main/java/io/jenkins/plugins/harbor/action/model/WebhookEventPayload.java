package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload of notification event
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/notifier/model/event.go">WebhookEventPayload</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookEventPayload {
    private EventType type;

    @JsonProperty("occur_at")
    private long occurAt;

    private String operator;

    @JsonProperty("event_data")
    private EventData eventData;

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public long getOccurAt() {
        return occurAt;
    }

    public void setOccurAt(long occurAt) {
        this.occurAt = occurAt;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }
}
