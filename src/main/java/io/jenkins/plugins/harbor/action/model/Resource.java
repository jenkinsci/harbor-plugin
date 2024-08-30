package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.jenkins.plugins.harbor.client.models.NativeReportSummary;

import java.util.HashMap;

/**
 * Resource describe infos of resource triggered notification
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/notifier/model/event.go">Resource</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings("EI_EXPOSE_REP")
public class Resource {
    private String digest;
    private String tag;

    @JsonProperty("resource_url")
    private String resourceURL;

    @JsonProperty("scan_overview")
    private HashMap<String, NativeReportSummary> ScanOverview;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public HashMap<String, NativeReportSummary> getScanOverview() {
        return ScanOverview;
    }

    public void setScanOverview(HashMap<String, NativeReportSummary> scanOverview) {
        ScanOverview = scanOverview;
    }
}
