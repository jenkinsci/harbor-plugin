package io.jenkins.plugins.harbor.client;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.jenkins.plugins.harbor.HarborException;
import io.jenkins.plugins.harbor.client.models.Artifact;
import io.jenkins.plugins.harbor.client.models.NativeReportSummary;
import io.jenkins.plugins.harbor.client.models.Repository;
import java.io.IOException;
import java.util.Map;

public interface HarborClient {
    /**
     * Get the vulnerabilities addition of the specific artifact
     * <p>
     * Get the vulnerabilities addition of the artifact specified by the reference under the project and repository.
     *
     * @param projectName    The project name of Harbor
     * @param repositoryName The name of
     * @param reference      The reference can be digest or tag.
     */
    NativeReportSummary getVulnerabilitiesAddition(String projectName, String repositoryName, String reference);

    /**
     * Ping Harbor to check if it's alive.
     * <p>
     * This API simply replies a pong to indicate the process to handle API is up,
     * disregarding the health status of dependent components.
     *
     * @return If the request succeeds, the 'Pong' string is returned
     * @throws IOException     The HTTP request failed
     * @throws HarborException httpUrl is null
     */
    String getPing() throws IOException;

    /**
     * List all authorized repositories
     *
     * @return Return to the list of repositories
     */
    Repository[] listAllRepositories() throws IOException;

    Artifact[] listArtifacts(String projectName, String repositoryName, @Nullable Map<String, String> extraParams)
            throws HarborException, IOException;

    /**
     * Get the specific artifact
     * <p>
     * Get the artifact specified by the reference under the project and repository.
     * The reference can be digest or tag.
     *
     * @return Artifact Return artifact information
     * @throws IOException     The HTTP request failed
     * @throws HarborException The apiUrl is null in getArtifact method
     */
    Artifact getArtifact(
            String projectName, String repositoryName, String reference, @Nullable Map<String, String> extraParams)
            throws IOException;
}
