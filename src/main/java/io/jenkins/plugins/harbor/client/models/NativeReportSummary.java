package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.jenkins.plugins.harbor.action.model.VulnerabilityScanStatus;
import java.util.ArrayList;
import java.util.Date;

/**
 * NativeReportSummary is the default supported scan report summary model.
 * Generated based on the report with v1.MimeTypeNativeReport or the v1.MimeTypeGenericVulnerabilityReport mime type.
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/scan/vuln/summary.go">NativeReportSummary</a>
 */
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class NativeReportSummary {
    private String reportId;

    private VulnerabilityScanStatus ScanStatus;

    private Severity severity;

    private long duration;

    private VulnerabilitySummary summary;

    private ArrayList<String> cvebypassed;

    private Date StartTime;

    private Date EndTime;

    private Scanner scanner;

    private int completePercent;

    @JsonIgnore
    private int totalCount;

    @JsonIgnore
    private int completeCount;

    @JsonIgnore
    private VulnerabilityItemList vulnerabilityItemList;

    public String getReportId() {
        return reportId;
    }

    @JsonProperty("report_id")
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public VulnerabilityScanStatus getScanStatus() {
        return ScanStatus;
    }

    @JsonProperty("scan_status")
    public void setScanStatus(VulnerabilityScanStatus scanStatus) {
        ScanStatus = scanStatus;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public VulnerabilitySummary getSummary() {
        return summary;
    }

    public void setSummary(VulnerabilitySummary summary) {
        this.summary = summary;
    }

    public ArrayList<String> getCvebypassed() {
        return cvebypassed;
    }

    @JsonIgnore
    public void setCvebypassed(ArrayList<String> cvebypassed) {
        this.cvebypassed = cvebypassed;
    }

    public Date getStartTime() {
        return StartTime;
    }

    @JsonProperty("start_time")
    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    @JsonProperty("end_time")
    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getCompletePercent() {
        return completePercent;
    }

    @JsonProperty("complete_percent")
    public void setCompletePercent(int completePercent) {
        this.completePercent = completePercent;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public VulnerabilityItemList getVulnerabilityItemList() {
        return vulnerabilityItemList;
    }

    public void setVulnerabilityItemList(VulnerabilityItemList vulnerabilityItemList) {
        this.vulnerabilityItemList = vulnerabilityItemList;
    }
}
