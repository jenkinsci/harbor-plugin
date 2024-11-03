package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Replication describes replication infos
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/event/model/event.go">Replication</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class Replication {
    private String harborHostname;
    private String jobStatus;
    private String description;
    private String artifactType;
    private String authenticationType;
    private boolean overrideMode;
    private String triggerType;
    private String policyCreator;
    private long executionTimestamp;
    private ReplicationResource srcResource;
    private ReplicationResource destResource;
    private ArtifactInfo[] successfulArtifact;
    private ArtifactInfo[] failedArtifact;

    public String getHarborHostname() {
        return harborHostname;
    }

    @JsonProperty("harbor_hostname")
    public void setHarborHostname(String harborHostname) {
        this.harborHostname = harborHostname;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    @JsonProperty("job_status")
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtifactType() {
        return artifactType;
    }

    @JsonProperty("artifact_type")
    public void setArtifactType(String artifactType) {
        this.artifactType = artifactType;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    @JsonProperty("authentication_type")
    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public boolean isOverrideMode() {
        return overrideMode;
    }

    @JsonProperty("override_mode")
    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }

    public String getTriggerType() {
        return triggerType;
    }

    @JsonProperty("trigger_type")
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getPolicyCreator() {
        return policyCreator;
    }

    @JsonProperty("policy_creator")
    public void setPolicyCreator(String policyCreator) {
        this.policyCreator = policyCreator;
    }

    public long getExecutionTimestamp() {
        return executionTimestamp;
    }

    @JsonProperty("execution_timestamp")
    public void setExecutionTimestamp(long executionTimestamp) {
        this.executionTimestamp = executionTimestamp;
    }

    public ReplicationResource getSrcResource() {
        return srcResource;
    }

    @JsonProperty("src_resource")
    public void setSrcResource(ReplicationResource srcResource) {
        this.srcResource = srcResource;
    }

    public ReplicationResource getDestResource() {
        return destResource;
    }

    @JsonProperty("dest_resource")
    public void setDestResource(ReplicationResource destResource) {
        this.destResource = destResource;
    }

    public ArtifactInfo[] getSuccessfulArtifact() {
        return successfulArtifact;
    }

    @JsonProperty("successful_artifact")
    public void setSuccessfulArtifact(ArtifactInfo[] successfulArtifact) {
        this.successfulArtifact = successfulArtifact;
    }

    public ArtifactInfo[] getFailedArtifact() {
        return failedArtifact;
    }

    @JsonProperty("failed_artifact")
    public void setFailedArtifact(ArtifactInfo[] failedArtifact) {
        this.failedArtifact = failedArtifact;
    }

    /**
     * Payload of notification event
     *
     * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/notifier/model/event.go">WebhookEventPayload</a>
     */
    public static class WebhookEventPayload {
        private EventType type;
        private long occurAt;
        private String operator;
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

        @JsonProperty("occur_at")
        public void setOccurAt(long occurAt) {
            this.occurAt = occurAt;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP")
        public EventData getEventData() {
            return eventData;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        @JsonProperty("event_data")
        public void setEventData(EventData eventData) {
            this.eventData = eventData;
        }
    }
}
