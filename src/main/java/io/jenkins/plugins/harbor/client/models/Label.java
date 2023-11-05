package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Label holds information used for a label
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/label/model/model.go">Label</a>
 */
public class Label {
    private long id;
    private String name;
    private String description;
    private String color;
    private String level;
    private String scope;

    @JsonProperty("project_id")
    private long projectId;

    @JsonProperty("creation_time")
    private Date creationTime;

    @JsonProperty("update_time")
    private Date updateTime;

    private boolean deleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @SuppressWarnings("unused")
    public long getProjectId() {
        return projectId;
    }

    @SuppressWarnings("unused")
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @SuppressWarnings("unused")
    public Date getCreationTime() {
        return creationTime;
    }

    @SuppressWarnings("unused")
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @SuppressWarnings("unused")
    public Date getUpdateTime() {
        return updateTime;
    }

    @SuppressWarnings("unused")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
