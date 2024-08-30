package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.HashMap;

/**
 * EventData of notification event payload
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/notifier/model/event.go">EventData</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class EventData {
    private Resource[] resources;
    private Repository repository;
    private io.jenkins.plugins.harbor.action.model.Replication Replication;
    private io.jenkins.plugins.harbor.action.model.Retention Retention;

    @JsonProperty("custom_attributes")
    private HashMap<String, String> Custom;

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Replication getReplication() {
        return Replication;
    }

    public void setReplication(Replication replication) {
        Replication = replication;
    }

    public Retention getRetention() {
        return Retention;
    }

    public void setRetention(Retention retention) {
        Retention = retention;
    }

    public HashMap<String, String> getCustom() {
        return Custom;
    }

    public void setCustom(HashMap<String, String> custom) {
        Custom = custom;
    }
}
