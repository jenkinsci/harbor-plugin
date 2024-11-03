package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ArtifactInfo describe info of artifact
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/event/model/event.go#L23">ArtifactInfo</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtifactInfo {
    private String type;
    private String status;

    @JsonProperty("name_tag")
    private String nameAndTag;

    @JsonProperty("fail_reason")
    private String failReason;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNameAndTag() {
        return nameAndTag;
    }

    public void setNameAndTag(String nameAndTag) {
        this.nameAndTag = nameAndTag;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
