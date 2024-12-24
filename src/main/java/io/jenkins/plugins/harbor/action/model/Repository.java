package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repository info of notification event
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/notifier/model/event.go">Repository</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    @JsonProperty("date_created")
    private long dateCreated;

    private String name;
    private String namespace;

    @JsonProperty("repo_full_name")
    private String repoFullName;

    @JsonProperty("repo_type")
    private String repoType;

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getRepoFullName() {
        return repoFullName;
    }

    public void setRepoFullName(String repoFullName) {
        this.repoFullName = repoFullName;
    }

    public String getRepoType() {
        return repoType;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }
}
