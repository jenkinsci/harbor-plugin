package io.jenkins.plugins.harbor.client;

import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HarborInterceptor implements Interceptor {
    private final String credential;

    public HarborInterceptor(String username, String password) {
        this.credential = Credentials.basic(username, password);
    }

    public HarborInterceptor(StandardUsernamePasswordCredentials credentials) {
        this.credential = Credentials.basic(
                credentials.getUsername(), credentials.getPassword().getPlainText());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .build();
        return chain.proceed(authenticatedRequest);
    }
}
