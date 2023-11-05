package io.jenkins.plugins.harbor.client.models;

/**
 * Scanner represents metadata of a Scanner Adapter which allow Harbor to lookup a scanner capable of
 * scanning a given Artifact stored in its registry and making sure that it can interpret a
 * returned result.
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/scan/rest/v1/models.go">Scanner</a>
 */
public class Scanner {
    private String name;
    private String vendor;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
