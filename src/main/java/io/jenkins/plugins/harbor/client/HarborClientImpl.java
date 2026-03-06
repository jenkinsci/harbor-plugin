package io.jenkins.plugins.harbor.client;

import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.damnhandy.uri.template.UriTemplate;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.cli.NoCheckTrustManager;
import io.jenkins.plugins.harbor.HarborException;
import io.jenkins.plugins.harbor.client.models.Artifact;
import io.jenkins.plugins.harbor.client.models.NativeReportSummary;
import io.jenkins.plugins.harbor.client.models.Repository;
import io.jenkins.plugins.harbor.util.HarborConstants;
import io.jenkins.plugins.harbor.util.JsonParser;
import io.jenkins.plugins.okhttp.api.JenkinsOkHttpClient;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

public class HarborClientImpl implements HarborClient {
    private static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 30;
    private static final int DEFAULT_READ_TIMEOUT_SECONDS = 30;
    private static final int DEFAULT_WRITE_TIMEOUT_SECONDS = 30;
    private static final String API_PING_PATH = "/ping";
    private static final String API_LIST_ALL_REPOSITORIES_PATH = "/repositories";
    private static final String API_LIST_ARTIFACTS_PATH =
            "/projects/{project_name}/repositories/{repository_name}/artifacts";
    private static final String API_GET_ARTIFACT_PATH =
            "/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}";
    private final String baseUrl;
    private final OkHttpClient httpClient;

    public HarborClientImpl(
            String baseUrl,
            StandardUsernamePasswordCredentials credentials,
            boolean isSkipTlsVerify,
            boolean debugLogging)
            throws NoSuchAlgorithmException, KeyManagementException {
        this.baseUrl = baseUrl;

        // Create Http client
        OkHttpClient.Builder httpClientBuilder = JenkinsOkHttpClient.newClientBuilder(new OkHttpClient());
        addTimeout(httpClientBuilder);
        skipTlsVerify(httpClientBuilder, isSkipTlsVerify);
        addAuthenticator(httpClientBuilder, credentials);
        enableDebugLogging(httpClientBuilder, debugLogging);
        this.httpClient = httpClientBuilder.build();
    }

    private OkHttpClient.Builder addTimeout(OkHttpClient.Builder httpClient) {
        httpClient.connectTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClient.readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClient.writeTimeout(DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClient.setRetryOnConnectionFailure$okhttp(true);
        return httpClient;
    }

    @SuppressWarnings("lgtm[jenkins/unsafe-calls]")
    private OkHttpClient.Builder skipTlsVerify(OkHttpClient.Builder httpClient, boolean isSkipTlsVerify)
            throws NoSuchAlgorithmException, KeyManagementException {
        if (isSkipTlsVerify) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = new TrustManager[] {new NoCheckTrustManager()};
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            httpClient.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]);
            httpClient.setHostnameVerifier$okhttp((hostname, session) -> true);
        }

        return httpClient;
    }

    private OkHttpClient.Builder addAuthenticator(
            OkHttpClient.Builder httpClientBuilder, StandardUsernamePasswordCredentials credentials) {
        if (credentials != null) {
            HarborInterceptor harborInterceptor = new HarborInterceptor(credentials);
            httpClientBuilder.addInterceptor(harborInterceptor);
        }

        return httpClientBuilder;
    }

    private OkHttpClient.Builder enableDebugLogging(OkHttpClient.Builder httpClient, boolean debugLogging) {
        if (debugLogging) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        return httpClient;
    }

    private String getXAcceptVulnerabilities() {
        return String.format(
                "%s, %s",
                HarborConstants.HarborVulnerabilityReportV11MimeType, HarborConstants.HarborVulnReportv1MimeType);
    }

    @Override
    public NativeReportSummary getVulnerabilitiesAddition(String projectName, String repositoryName, String reference) {
        return null;
    }

    @Override
    public String getPing() throws IOException {
        UriTemplate template = UriTemplate.fromTemplate(baseUrl + API_PING_PATH);
        HttpUrl apiUrl = HttpUrl.parse(template.expand());
        if (apiUrl != null) {
            return httpGet(apiUrl);
        }

        throw new HarborException(String.format("httpUrl is null, UriTemplate is %s.", template.expand()));
    }

    @Override
    public Repository[] listAllRepositories() throws IOException {
        HttpUrl apiUrl = addQueryParameter(
                UriTemplate.fromTemplate(baseUrl + API_LIST_ALL_REPOSITORIES_PATH), new HashMap<String, String>());
        return JsonParser.toJava(httpGet(apiUrl), Repository[].class);
    }

    @Override
    public Artifact[] listArtifacts(
            String projectName, String repositoryName, @Nullable Map<String, String> extraParams)
            throws HarborException, IOException {
        // repository name must be encoded twice as per Harbor API v2 specifications
        String encodedRepositoryName = URLEncoder.encode(repositoryName, StandardCharsets.UTF_8);
        HttpUrl apiUrl = addQueryParameter(
                UriTemplate.fromTemplate(baseUrl + API_LIST_ARTIFACTS_PATH)
                        .set("project_name", projectName)
                        .set("repository_name", encodedRepositoryName),
                extraParams);
        return JsonParser.toJava(httpGet(apiUrl), Artifact[].class);
    }

    @Override
    public Artifact getArtifact(
            String projectName, String repositoryName, String reference, @Nullable Map<String, String> extraParams)
            throws IOException {
        // repository name must be encoded twice as per Harbor API v2 specifications
        String encodedRepositoryName = URLEncoder.encode(repositoryName, StandardCharsets.UTF_8);
        HttpUrl apiUrl = addQueryParameter(
                UriTemplate.fromTemplate(baseUrl + API_GET_ARTIFACT_PATH)
                        .set("project_name", projectName)
                        .set("repository_name", encodedRepositoryName)
                        .set("reference", reference),
                extraParams);
        return JsonParser.toJava(httpGet(apiUrl), Artifact.class);
    }

    private HttpUrl addQueryParameter(UriTemplate template, @Nullable Map<String, String> extraParams) {
        HttpUrl httpUrl = HttpUrl.parse(template.expand());
        if (httpUrl != null) {
            HttpUrl.Builder httpBuilder = httpUrl.newBuilder();
            if (extraParams != null) {
                for (Map.Entry<String, String> param : extraParams.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
            }
            return httpBuilder.build();
        } else {
            throw new HarborException(String.format("httpUrl is null, UriTemplate is %s.", template.expand()));
        }
    }

    private String httpGet(HttpUrl httpUrl) throws IOException {
        Request request = new Request.Builder()
                .url(httpUrl.url())
                .header("X-Accept-Vulnerabilities", getXAcceptVulnerabilities())
                .build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            if (response.isSuccessful()) {
                return responseBody.string();
            } else {
                throw new HarborRequestException(
                        response.code(),
                        String.format(
                                "The server(%s) api response failed, Response message is '%s'.",
                                httpUrl, responseBody.string()));
            }
        } else {
            throw new HarborRequestException(
                    response.code(),
                    String.format(
                            "httpGet method get responseBody failed, The request api is %s, Response message is '%s'",
                            httpUrl, response.message()));
        }
    }
}
