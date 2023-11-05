package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Tag is the overall view of tag
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/tag/model.go">Tag</a>
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/tag/model/tag/model.go">Tag</a>
 */
public class Tag {
    private long id;

    @JsonProperty("repository_id")
    private long repositoryId;

    @JsonProperty("artifact_id")
    private long artifactId;

    private String name;

    @JsonProperty("push_time")
    private Date pushTime;

    @JsonProperty("pull_time")
    private Date pullTime;

    private boolean immutable;
    private boolean signed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(long artifactId) {
        this.artifactId = artifactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPushTime() {
        if (pushTime != null) {
            return new Date(pushTime.getTime());
        } else {
            return null;
        }
    }

    public void setPushTime(Date pushTime) {
        if (pushTime != null) {
            this.pushTime = new Date(pushTime.getTime());
        } else {
            this.pushTime = null;
        }
    }

    public Date getPullTime() {
        if (pullTime != null) {
            return new Date(pullTime.getTime());
        } else {
            return null;
        }
    }

    public void setPullTime(Date pullTime) {
        if (pullTime != null) {
            this.pushTime = new Date(pullTime.getTime());
        } else {
            this.pushTime = null;
        }
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }
}
