package io.jenkins.plugins.harbor.configuration;

import hudson.Extension;
import io.jenkins.plugins.harbor.HarborException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class HarborPluginGlobalConfiguration extends GlobalConfiguration implements Serializable {
    private static final Logger logger = Logger.getLogger(HarborPluginGlobalConfiguration.class.getName());

    private List<HarborServer> servers;

    public HarborPluginGlobalConfiguration() {
        load();
    }

    public static HarborPluginGlobalConfiguration get() {
        return GlobalConfiguration.all().get(HarborPluginGlobalConfiguration.class);
    }

    public static HarborServer getHarborServerByName(String name) {
        return get().getServers().stream()
                .filter(harborServer -> StringUtils.equals(name, harborServer.getName()))
                .findFirst()
                .orElseThrow(() -> new HarborException("The Harbor Server Name Is Invalid"));
    }

    public List<HarborServer> getServers() {
        return servers;
    }

    public void setServers(List<HarborServer> servers) {
        this.servers = servers;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }
}
