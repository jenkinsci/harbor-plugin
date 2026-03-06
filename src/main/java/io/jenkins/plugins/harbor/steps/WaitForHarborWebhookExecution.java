package io.jenkins.plugins.harbor.steps;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.AbortException;
import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ArgumentListBuilder;
import io.jenkins.plugins.harbor.HarborException;
import io.jenkins.plugins.harbor.action.HarborBuildBadgeAction;
import io.jenkins.plugins.harbor.action.HarborWebHookAction;
import io.jenkins.plugins.harbor.action.HarborWebhookEvent;
import io.jenkins.plugins.harbor.action.model.EventType;
import io.jenkins.plugins.harbor.action.model.Resource;
import io.jenkins.plugins.harbor.action.model.VulnerabilityScanStatus;
import io.jenkins.plugins.harbor.client.HarborClientImpl;
import io.jenkins.plugins.harbor.client.models.Artifact;
import io.jenkins.plugins.harbor.client.models.NativeReportSummary;
import io.jenkins.plugins.harbor.client.models.Severity;
import io.jenkins.plugins.harbor.configuration.HarborPluginGlobalConfiguration;
import io.jenkins.plugins.harbor.configuration.HarborServer;
import io.jenkins.plugins.harbor.util.HarborConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.support.actions.PauseAction;

public class WaitForHarborWebhookExecution extends StepExecution implements Consumer<HarborWebhookEvent> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(WaitForHarborWebhookExecution.class.getName());
    private static final int MAX_LOG_LINES = 1000;
    private static final String BUILD_IMAGE_NAME_PATTERN = "#\\d+ naming to (\\S+/\\S+/\\S+:\\S+) .*done";
    private static final String BUILD_IMAGE_DIGEST_PATTERN = "(\\d+): digest: (sha256:[a-f0-9]+) size: (\\d+)";
    private static final String BUILDX_IMAGE_DIGEST_PATTERN = "#\\d+: writing image (sha256:[a-f0-9]+) .*done";
    private final WaitForHarborWebhookStep waitForHarborWebhookStep;
    private Image image;

    public WaitForHarborWebhookExecution(StepContext context, WaitForHarborWebhookStep waitForHarborWebhookStep) {
        super(context);
        this.waitForHarborWebhookStep = waitForHarborWebhookStep;
    }

    @Override
    public boolean start() throws Exception {
        processStepParameters();
        if (!checkScanCompleted()) {
            HarborWebhookEvent harborWebhookEvent =
                    HarborWebHookAction.get().getWebhookEventForDigest(image.getImageDigest());
            if (harborWebhookEvent != null) {
                validateWebhookAndCheckSeverityIfValid(harborWebhookEvent, true);
                return true;
            } else {
                getContextClass(FlowNode.class).addAction(new PauseAction("Harbor Scanner analysis"));
                return false;
            }
        } else {
            return true;
        }
    }

    private void processStepParameters() throws IOException {
        String foundImageName = waitForHarborWebhookStep.getFullImageName();
        String foundImageDigest = null;

        if (foundImageName == null) {
            List<String> lastConsoleLogLines = getContextClass(Run.class).getLog(MAX_LOG_LINES);
            Pattern namePattern = Pattern.compile(BUILD_IMAGE_NAME_PATTERN);
            Pattern digestPattern = Pattern.compile(BUILD_IMAGE_DIGEST_PATTERN);
            Pattern buildxDigestPattern = Pattern.compile(BUILDX_IMAGE_DIGEST_PATTERN);

            for (String line : lastConsoleLogLines) {
                Matcher nameMatcher = namePattern.matcher(line);
                Matcher digestMatcher = digestPattern.matcher(line);
                Matcher buildxDigestMatcher = buildxDigestPattern.matcher(line);

                if (nameMatcher.find()) {
                    foundImageName = nameMatcher.group(1);
                }

                if (digestMatcher.find()) {
                    foundImageDigest = digestMatcher.group(2);
                }

                if (buildxDigestMatcher.find()) {
                    foundImageDigest = buildxDigestMatcher.group(1);
                }
            }
        }
        if(foundImageName != null && foundImageDigest == null) {
            foundImageDigest = getDigestByFullImageName(foundImageName);
        }

        if (foundImageName == null || foundImageDigest == null) {
            throw new ImageInfoExtractionException(String.format(
                    "Failed to extract image name(%s) or digest(%s). Image not found.",
                    foundImageName, foundImageDigest));
        }

        this.image = new Image(foundImageName, foundImageDigest);
        getContextClass(Run.class)
                .addAction(new HarborBuildBadgeAction(String.format(
                        "https://%s/harbor/projects/%s/repositories/%s/artifacts-tab/artifacts/%s",
                        image.getRegistry(), image.getProjects(), image.getRepository(), foundImageDigest)));
    }

    private String getDigestByFullImageName(String fullImageName) {
        ArgumentListBuilder argumentListBuilder = new ArgumentListBuilder();
        argumentListBuilder.add("docker", "inspect", "-f", "{{.RepoDigests}}", fullImageName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        try {
            int status = getContextClass(Launcher.class)
                    .launch()
                    .cmds(argumentListBuilder)
                    .envs(getContextClass(EnvVars.class))
                    .quiet(true)
                    .stdout(output)
                    .stderr(error)
                    .start()
                    .join();
            if (status == 0) {
                String charsetName = Charset.defaultCharset().name();
                String repoDigests = output.toString(charsetName).trim();
                for (String repoDigest :
                        repoDigests.substring(1, repoDigests.length() - 1).split(" ")) {
                    if (repoDigest.startsWith(fullImageName.split(":")[0])) {
                        return repoDigest.split("@")[1];
                    }
                }

                throw new HarborException(
                        String.format("Unable to get matching image digest. repoDigests: %s%n", repoDigests));
            } else {
                throw new HarborException("Run docker command fail, Unable to get image digest.");
            }
        } catch (UnsupportedEncodingException e) {
            throw new HarborException("Encoding error, unable to read command result.", e);
        } catch (IOException | InterruptedException e) {
            throw new HarborException("Run command error, Unable to get command execution results", e);
        }
    }

    private boolean checkScanCompleted() {
        HarborWebHookAction.get().addListener(this);
        writeLogToConsole(
                "Checking scan status of Harbor artifact '%s' on server '%s'%n",
                image.getImageName(), image.getRegistry());

        try {
            HarborServer harborServer =
                    HarborPluginGlobalConfiguration.getHarborServerByName(waitForHarborWebhookStep.getServer());
            HarborClientImpl harborAPIClient = new HarborClientImpl(
                    harborServer.getBaseUrl(),
                    getCredentials(waitForHarborWebhookStep.getCredentialsId()),
                    harborServer.isSkipTlsVerify(),
                    harborServer.isDebugLogging());
            HashMap<String, String> extraParams = new HashMap<String, String>() {
                {
                    put("with_scan_overview", "true");
                    put("page_size", "15");
                    put("page", "1");
                }
            };
            Artifact artifact = harborAPIClient.getArtifact(
                    image.getProjects(), image.getRepository(), image.getImageDigest(), extraParams);

            logger.info(artifact.toString());
            HashMap<String, NativeReportSummary> scanOverview = artifact.getScanOverview();
            if (scanOverview != null && !scanOverview.isEmpty()) {
                NativeReportSummary nativeReportSummary =
                        scanOverview.get(HarborConstants.HarborVulnerabilityReportV11MimeType);
                return checkScanStatus(nativeReportSummary.getScanStatus(), nativeReportSummary.getSeverity(), true);
            }
            writeLogToConsole(
                    "The Artifact api cannot get scan overview, Please check whether you have enabled image scanning.");
            return false;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HarborException("Connect to harbor server Failed.", e);
        } catch (IOException e) {
            throw new HarborException("Interrupted on checkScanCompleted.", e);
        }
    }

    private StandardUsernamePasswordCredentials getCredentials(String credentialsId) {
        return CredentialsProvider.findCredentialById(
                credentialsId,
                StandardUsernamePasswordCredentials.class,
                Objects.requireNonNull(getContextClass(Run.class)),
                Collections.emptyList());
    }

    public boolean checkScanStatus(VulnerabilityScanStatus scanStatus, Severity severity, boolean onStart) {
        writeLogToConsole("Harbor artifact '%s' scan result is '%s'%n", image.getImageName(), scanStatus);
        switch (scanStatus) {
            case SUCCESS:
                writeLogToConsole(
                        "Harbor artifact '%s' scan completed. Severity is %s%n", image.getImageName(), severity);
                handleSeverity(severity);
                return true;
            case STOPPED:
            case ERROR:
            case NOT_SCANNED:
                HarborException exception =
                        new HarborException("Trivy analysis '" + image.getImageName() + "' failed: " + scanStatus);
                if (onStart) {
                    throw exception;
                } else {
                    getContext().onFailure(exception);
                    return true;
                }
            default:
                if (onStart) {
                    return false;
                } else {
                    throw new IllegalStateException("Unexpected task status: " + scanStatus);
                }
        }
    }

    private void handleSeverity(Severity severity) {
        Severity severityConfig = waitForHarborWebhookStep.getSeverity();
        List<Severity> severityList = Arrays.asList(Severity.values());
        List<Severity> severityConfigList =
                severityList.subList(severityList.indexOf(severityConfig), severityList.size());
        boolean isRisk = severityConfigList.contains(severity);

        if (waitForHarborWebhookStep.isAbortPipeline() && isRisk) {
            getContext().onFailure(new AbortException("Pipeline aborted due to severity failure: " + severity));
        } else {
            getContext().onSuccess(severity);
        }
    }

    private void validateWebhookAndCheckSeverityIfValid(HarborWebhookEvent harborWebhookEvent, boolean onStart) {
        HarborWebHookAction.get().removeListener(this);
        if (validateWebhook(harborWebhookEvent)) {
            // only execute the checkScanStatus if the webhook is found to be valid (getContext().onFailure() does not
            // interrupt execution)
            Resource[] resources =
                    harborWebhookEvent.getWebhookEventPayload().getEventData().getResources();
            for (Resource resource : resources) {
                HashMap<String, NativeReportSummary> scanOverview = resource.getScanOverview();
                if (scanOverview != null) {
                    NativeReportSummary nativeReportSummary =
                            scanOverview.get(HarborConstants.HarborVulnerabilityReportV11MimeType);
                    checkScanStatus(nativeReportSummary.getScanStatus(), nativeReportSummary.getSeverity(), onStart);
                } else {
                    throw new HarborException("The harbor webhook payload exception, Unable to get scan overview.");
                }
            }
        }
    }

    private boolean validateWebhook(HarborWebhookEvent harborWebhookEvent) {
        return true;
    }

    @Override
    public void stop(@NonNull Throwable cause) throws Exception {
        PauseAction.endCurrentPause(getContextClass(FlowNode.class));
        HarborWebHookAction.get().removeListener(this);
        super.stop(cause);
    }

    @Override
    public void onResume() {
        HarborWebHookAction.get().addListener(this);
        try {
            checkScanCompleted();
            super.onResume();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to restore step", e);
        }
    }

    @Override
    public void accept(HarborWebhookEvent harborWebhookEvent) {
        if (harborWebhookEvent.getDigest().equals(image.getImageDigest())) {
            EventType eventType = harborWebhookEvent.getWebhookEventPayload().getType();
            if (eventType == EventType.SCANNING_COMPLETED) {
                try {
                    logger.info(String.format(
                            "Handle %s's Harbor Webhook event(%s)%n", harborWebhookEvent.getImageName(), eventType));
                    HarborWebHookAction.getEventCache().put(harborWebhookEvent.getDigest(), harborWebhookEvent);
                    PauseAction.endCurrentPause(getContextClass(FlowNode.class));
                    validateWebhookAndCheckSeverityIfValid(harborWebhookEvent, false);
                } catch (IOException e) {
                    getContext().onFailure(e);
                    throw new IllegalStateException(e);
                }
            } else {
                logger.info(String.format(
                        "Ignore %s's Harbor Webhook event(%s)%n", harborWebhookEvent.getImageName(), eventType));
            }
        } else {
            logger.info("harborWebhookEvent::getDigest: " + harborWebhookEvent.getDigest());
            logger.warning(
                    "Ignore docker image: " + image.getImageName() + ", digest: " + image.getImageDigest() + ".");
        }
    }

    private void writeLogToConsole(String msg, Object... args) {
        getContextClass(TaskListener.class).getLogger().printf(msg, args);
    }

    private <T> T getContextClass(Class<T> contextClass) {
        try {
            return Optional.ofNullable(getContext().get(contextClass))
                    .orElseThrow(() -> new IllegalStateException(
                            String.format("Could not get %s from the Jenkins context", contextClass.getName())));
        } catch (IOException | IllegalStateException e) {
            getContext().onFailure(e);
            throw new IllegalStateException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            getContext().onFailure(e);
            throw new IllegalStateException(e);
        }
    }
}
