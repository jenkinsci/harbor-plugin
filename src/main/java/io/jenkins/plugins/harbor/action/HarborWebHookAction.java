package io.jenkins.plugins.harbor.action;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import hudson.Extension;
import hudson.model.RootAction;
import hudson.model.UnprotectedRootAction;
import hudson.security.csrf.CrumbExclusion;
import io.jenkins.plugins.harbor.action.model.WebhookEventPayload;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jenkins.model.Jenkins;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

@Extension
public class HarborWebHookAction extends CrumbExclusion implements UnprotectedRootAction {
    private static final Logger logger = Logger.getLogger(HarborWebHookAction.class.getName());
    private static final String URL_NAME = "harbor-webhook";
    private static final Cache<String, HarborWebhookEvent> eventCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .recordStats()
            .build();
    List<Consumer<HarborWebhookEvent>> listeners = new CopyOnWriteArrayList<>();

    public static HarborWebHookAction get() {
        return Jenkins.get().getExtensionList(RootAction.class).get(HarborWebHookAction.class);
    }

    public static Cache<String, HarborWebhookEvent> getEventCache() {
        return eventCache;
    }

    /**
     * Harbor WebHook API
     *
     * @param req StaplerRequest
     * @param rsp StaplerResponse
     * @throws IOException Invalid JSON Payload
     * @see <a href="https://wiki.jenkins-ci.org/display/JENKINS/Web+Method">Web+Method</a>
     */
    @RequirePOST
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
        String payload = IOUtils.toString(req.getReader());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WebhookEventPayload webhookEventPayload = objectMapper.readValue(payload, WebhookEventPayload.class);
            HarborWebhookEvent harborWebhookEvent = new HarborWebhookEvent(webhookEventPayload);
            for (Consumer<HarborWebhookEvent> listener : listeners) {
                listener.accept(harborWebhookEvent);
            }
        } catch (JacksonException e) {
            logger.log(Level.FINE, String.format("Invalid JSON Payload(%s)%n", payload), e);
            rsp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON Payload");
        } finally {
            logger.info("Received POST from: " + req.getRemoteHost() + ", Payload: " + payload);
        }

        rsp.setStatus(HttpServletResponse.SC_OK);
    }

    public void addListener(Consumer<HarborWebhookEvent> harborWebhookEventConsumer) {
        listeners.add(harborWebhookEventConsumer);
    }

    public void removeListener(Consumer<HarborWebhookEvent> harborWebhookEventConsumer) {
        listeners.remove(harborWebhookEventConsumer);
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getUrlName() {
        return URL_NAME;
    }

    @Override
    public boolean process(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/" + URL_NAME + "/")) {
            chain.doFilter(request, response);
            return true;
        }

        return false;
    }

    public HarborWebhookEvent getWebhookEventForDigest(String digest) {
        return eventCache.getIfPresent(digest);
    }
}
