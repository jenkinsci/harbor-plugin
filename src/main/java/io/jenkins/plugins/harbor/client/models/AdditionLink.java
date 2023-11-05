package io.jenkins.plugins.harbor.client.models;

/**
 * AdditionLink is a link via that the addition can be fetched
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/controller/artifact/model.go">AdditionLink</a>
 */
public class AdditionLink {
    private String href;
    private boolean absolute;

    public boolean isAbsolute() {
        return absolute;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
