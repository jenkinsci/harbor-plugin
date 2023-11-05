package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/scan/vuln/severity.go">Severity</a>
 */
public enum Severity {
    None("None"),
    Unknown("Unknown"),
    Negligible("Negligible"),
    Low("Low"),
    Medium("Medium"),
    High("High"),
    Critical("Critical");

    @JsonValue
    private final String severity;

    Severity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }
}
