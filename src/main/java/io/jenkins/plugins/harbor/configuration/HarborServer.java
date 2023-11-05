package io.jenkins.plugins.harbor.configuration;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class HarborServer extends AbstractDescribableImpl<HarborServer> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(HarborServer.class.getName());
    private String name;
    private String baseUrl;
    private String webhookSecretId;
    private boolean skipTlsVerify = false;
    private boolean debugLogging = false;

    @DataBoundConstructor
    public HarborServer(
            String name, String baseUrl, String webhookSecretId, boolean skipTlsVerify, boolean debugLogging) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.webhookSecretId = webhookSecretId;
        this.skipTlsVerify = skipTlsVerify;
        this.debugLogging = debugLogging;
    }

    public String getName() {
        return name;
    }

    @DataBoundSetter
    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @DataBoundSetter
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getWebhookSecretId() {
        return webhookSecretId;
    }

    @DataBoundSetter
    public void setWebhookSecretId(String webhookSecretId) {
        this.webhookSecretId = webhookSecretId;
    }

    public boolean isSkipTlsVerify() {
        return skipTlsVerify;
    }

    @DataBoundSetter
    public void setSkipTlsVerify(boolean skipTlsVerify) {
        this.skipTlsVerify = skipTlsVerify;
    }

    public boolean isDebugLogging() {
        return debugLogging;
    }

    @DataBoundSetter
    public void setDebugLogging(boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<HarborServer> {
        /**
         * Checks that the supplied URL is valid.
         *
         * @param value the URL to check.
         * @return the validation results.
         */
        public static FormValidation doCheckBaseUrl(@QueryParameter String value) {
            try {
                new URL(value);
            } catch (MalformedURLException e) {
                return FormValidation.error("Invalid URL: " + e.getMessage());
            }
            return FormValidation.ok();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillWebhookSecretIdItems(@QueryParameter String webhookSecretId) {
            if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                return new StandardListBoxModel().includeCurrentValue(webhookSecretId);
            }

            return new StandardListBoxModel()
                    .includeEmptyValue()
                    .includeMatchingAs(
                            ACL.SYSTEM,
                            Jenkins.get(),
                            StringCredentials.class,
                            Collections.emptyList(),
                            CredentialsMatchers.always());
        }

        @Override
        public String getDisplayName() {
            return "Harbor Server";
        }
    }
}
