package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Date;
import java.util.HashMap;

/**
 * Artifact
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/artifact/model.go">Artifact</a>
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/artifact/model.go">Artifact</a>
 */
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class Artifact {
    private long id;
    private String type;
    private String mediaType;
    private String manifestMediaType;
    private String projectId;
    private String repositoryId;
    private String repositoryName;
    private String digest;
    private long size;
    private String icon;
    private Date pushTime;
    private Date pullTime;
    private HashMap<String, Object> extraAttrs;
    private String annotations;
    private String references;

    @JsonIgnore
    private String tags;

    @JsonIgnore
    private HashMap<String, AdditionLink> additionLinks;

    private String labels;
    private String accessories;
    private HashMap<String, NativeReportSummary> scanOverview;

    public String getMediaType() {
        return mediaType;
    }

    @JsonProperty("media_type")
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getPushTime() {
        return pushTime;
    }

    @JsonProperty("push_time")
    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public HashMap<String, NativeReportSummary> getScanOverview() {
        return scanOverview;
    }

    @JsonProperty("scan_overview")
    public void setScanOverview(HashMap<String, NativeReportSummary> scanOverview) {
        this.scanOverview = scanOverview;
    }

    public Date getPullTime() {
        return pullTime;
    }

    @JsonProperty("pull_time")
    public void setPullTime(Date pullTime) {
        this.pullTime = pullTime;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getManifestMediaType() {
        return manifestMediaType;
    }

    @JsonProperty("manifest_media_type")
    public void setManifestMediaType(String manifestMediaType) {
        this.manifestMediaType = manifestMediaType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    @JsonProperty("repository_id")
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public HashMap<String, AdditionLink> getAdditionLinks() {
        return additionLinks;
    }

    @JsonProperty("addition_links")
    public void setAdditionLinks(HashMap<String, AdditionLink> additionLinks) {
        this.additionLinks = additionLinks;
    }

    public String getProjectId() {
        return projectId;
    }

    @JsonProperty("project_id")
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public HashMap<String, Object> getExtraAttrs() {
        return extraAttrs;
    }

    @JsonProperty("extra_attrs")
    public void setExtraAttrs(HashMap<String, Object> extraAttrs) {
        this.extraAttrs = extraAttrs;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    @JsonProperty("repository_name")
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
