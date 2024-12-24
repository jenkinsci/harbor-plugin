package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Retention describes tag retention infos
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/event/model/event.go">Retention</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class Retention {
    private int total;
    private int retained;

    @JsonProperty("harbor_hostname")
    private String harborHostname;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("retention_policy_id")
    private long retentionPolicyID;

    @JsonProperty("retention_rule")
    private RetentionRule[] retentionRules;

    private String Status;

    @JsonProperty("deleted_artifact")
    private ArtifactInfo[] deletedArtifact;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRetained() {
        return retained;
    }

    public void setRetained(int retained) {
        this.retained = retained;
    }

    public String getHarborHostname() {
        return harborHostname;
    }

    public void setHarborHostname(String harborHostname) {
        this.harborHostname = harborHostname;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getRetentionPolicyID() {
        return retentionPolicyID;
    }

    public void setRetentionPolicyID(long retentionPolicyID) {
        this.retentionPolicyID = retentionPolicyID;
    }

    public RetentionRule[] getRetentionRules() {
        return retentionRules;
    }

    public void setRetentionRules(RetentionRule[] retentionRules) {
        this.retentionRules = retentionRules;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ArtifactInfo[] getDeletedArtifact() {
        return deletedArtifact;
    }

    public void setDeletedArtifact(ArtifactInfo[] deletedArtifact) {
        this.deletedArtifact = deletedArtifact;
    }
}
