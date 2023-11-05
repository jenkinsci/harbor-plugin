package io.jenkins.plugins.harbor.steps;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.google.common.collect.ImmutableSet;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Item;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.harbor.client.models.Severity;
import io.jenkins.plugins.harbor.configuration.HarborPluginGlobalConfiguration;
import io.jenkins.plugins.harbor.configuration.HarborServer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class WaitForHarborWebhookStep extends Step implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(WaitForHarborWebhookStep.class.getName());
    private String server;
    private String credentialsId;
    private String fullImageName;
    private Severity severity;
    private boolean abortPipeline = true;

    @DataBoundConstructor
    public WaitForHarborWebhookStep(
            String server, String credentialsId, String fullImageName, Severity severity, boolean abortPipeline) {
        this.server = server;
        this.credentialsId = credentialsId;
        this.fullImageName = fullImageName;
        this.severity = severity;
        this.abortPipeline = abortPipeline;
    }

    public String getServer() {
        return server;
    }

    @DataBoundSetter
    public void setServer(String server) {
        this.server = server;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    @DataBoundSetter
    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    public Severity getSeverity() {
        return severity;
    }

    @DataBoundSetter
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getFullImageName() {
        return fullImageName;
    }

    @DataBoundSetter
    public void setFullImageName(String fullImageName) {
        this.fullImageName = fullImageName;
    }

    public boolean isAbortPipeline() {
        return abortPipeline;
    }

    @DataBoundSetter
    public void setAbortPipeline(boolean abortPipeline) {
        this.abortPipeline = abortPipeline;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new WaitForHarborWebhookExecution(context, this);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {
        @Override
        public String getFunctionName() {
            return "waitForHarborWebHook";
        }

        @Override
        public Set<Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, TaskListener.class, Run.class, Launcher.class, EnvVars.class);
        }

        public ListBoxModel doFillServerItems() {
            StandardListBoxModel result = new StandardListBoxModel();
            for (HarborServer harborServer :
                    HarborPluginGlobalConfiguration.get().getServers()) {
                result.add(harborServer.getName());
            }
            return result;
        }

        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item item) {
            if (item == null || !item.hasPermission(Item.CONFIGURE)) {
                return new ListBoxModel();
            }

            StandardListBoxModel model = new StandardListBoxModel();
            model.withEmptySelection();
            model.withMatching(
                    CredentialsMatchers.instanceOf(UsernamePasswordCredentials.class),
                    CredentialsProvider.lookupCredentials(
                            StandardUsernamePasswordCredentials.class, item, ACL.SYSTEM, Collections.emptyList()));
            return model;
        }

        public ListBoxModel doFillSeverityItems() {
            ListBoxModel items = new ListBoxModel();
            Severity[] severities = Severity.values();
            Collections.reverse(Arrays.asList(severities));
            List<Severity> severityBlackList = Arrays.asList(Severity.Negligible, Severity.Unknown);
            for (Severity severity : severities) {
                if (!severityBlackList.contains(severity)) {
                    items.add(severity.getSeverity());
                }
            }

            return items;
        }
    }
}
