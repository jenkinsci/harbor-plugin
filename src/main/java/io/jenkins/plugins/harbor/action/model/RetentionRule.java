package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.HashMap;

/**
 * RetentionRule describes tag retention rule
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/event/model/event.go">RetentionRule</a>
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/retention/policy/rule/models.go">Parameters</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class RetentionRule {
    private String template;

    @JsonProperty("params")
    private HashMap<String, HashMap<String, Object>> parameters;

    @JsonProperty("tag_selectors")
    private Selector[] tagSelectors;

    @JsonProperty("scope_selectors")
    private HashMap<String, Selector[]> scopeSelectors;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public HashMap<String, HashMap<String, Object>> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, HashMap<String, Object>> parameters) {
        this.parameters = parameters;
    }

    public Selector[] getTagSelectors() {
        return tagSelectors;
    }

    public void setTagSelectors(Selector[] tagSelectors) {
        this.tagSelectors = tagSelectors;
    }

    public HashMap<String, Selector[]> getScopeSelectors() {
        return scopeSelectors;
    }

    public void setScopeSelectors(HashMap<String, Selector[]> scopeSelectors) {
        this.scopeSelectors = scopeSelectors;
    }
}
