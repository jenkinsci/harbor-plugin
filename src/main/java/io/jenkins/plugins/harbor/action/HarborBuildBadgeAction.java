package io.jenkins.plugins.harbor.action;

import hudson.model.BuildBadgeAction;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean
public class HarborBuildBadgeAction implements BuildBadgeAction {
    private final String urlName;

    public HarborBuildBadgeAction(String urlName) {
        this.urlName = urlName;
    }

    @Override
    public String getIconFileName() {
        return "/plugin/harbor/images/harbor.svg";
    }

    @Override
    public String getDisplayName() {
        return "Harbor";
    }

    @Override
    @Exported(visibility = 2)
    public String getUrlName() {
        return urlName;
    }
}
