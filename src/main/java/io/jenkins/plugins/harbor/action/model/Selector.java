package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Selector to narrow down the list
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/immutable/model/rule.go">Selector</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Selector {
    private String kind;
    private String decoration;
    private String pattern;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
