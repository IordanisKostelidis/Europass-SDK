package eu.jnksoftware.europass.sdk.utils;

import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;

import java.io.InputStream;
import java.util.Objects;

public abstract class NetworkUtils {
    public static Try<HttpClient> constructHttpClient(final SSLConnectionSocketFactory sslConnectionSocketFactory) {
        return Try.run(() -> Objects.requireNonNull(sslConnectionSocketFactory, "sslConnectionSocketFactory is null"))
                .flatMap(ignored -> Try.of(HttpClientBuilder::create))
                .flatMap(httpClientBuilder -> Try.of(() -> httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory)))
                .flatMap(httpClientBuilder -> Try.of(httpClientBuilder::build));
    }

    public static Try<HttpEntity> constructHttpEntity(final InputStream inputStream) {
        return Try.run(() -> Objects.requireNonNull(inputStream, "inputStream is null"))
                .flatMap(ignored -> Try.of(() -> IOUtils.toByteArray(inputStream)))
                .flatMap(bytes -> Try.of(() -> new ByteArrayEntity(bytes)));
    }
}
