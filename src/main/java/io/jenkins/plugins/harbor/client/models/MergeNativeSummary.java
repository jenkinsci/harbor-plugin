package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/scan/report/summary.go">MergeNativeSummary</a>
 */
public class MergeNativeSummary {
    @JsonProperty("application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0")
    private NativeReportSummary mimeTypeNativeReport;

    @JsonProperty("application/vnd.security.vulnerability.report; version=1.1")
    private NativeReportSummary mimeTypeGenericVulnerabilityReport;
}
