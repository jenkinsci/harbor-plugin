package io.jenkins.plugins.harbor.action.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ReplicationResource describes replication resource info
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/event/model/event.go">ReplicationResource</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplicationResource {
    @JsonProperty("registry_name")
    private String registryName;

    @JsonProperty("registry_type")
    private String registryType;

    private String endpoint;
    private String provider;
    private String namespace;

    public String getRegistryName() {
        return registryName;
    }

    public void setRegistryName(String registryName) {
        this.registryName = registryName;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
