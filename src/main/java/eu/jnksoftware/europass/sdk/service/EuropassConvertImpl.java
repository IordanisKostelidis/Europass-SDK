package eu.jnksoftware.europass.sdk.service;

import eu.jnksoftware.europass.sdk.config.Config;
import eu.jnksoftware.europass.sdk.config.DefaultConfig;
import eu.jnksoftware.europass.sdk.constants.Conversion;
import eu.jnksoftware.europass.sdk.utils.FileUtils;
import eu.jnksoftware.europass.sdk.utils.NetworkUtils;
import eu.jnksoftware.europass.sdk.utils.SSLUtils;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class EuropassConvertImpl implements EuropassConvert {

    @Override
    public Try<File> convert(final File input,
                             final Conversion conversion,
                             final Config config) {
        return FileUtils.constructInputStream(input)
                .flatMap(NetworkUtils::constructHttpEntity)
                .flatMap(httpEntity -> constructPostRequest(conversion, config)
                        .flatMap(httpPost -> Try.of(() -> {
                            httpPost.setEntity(httpEntity);
                            return httpPost;
                        })))
                .flatMap(httpPost -> constructContentType(input)
                        .map(contentType -> {
                            httpPost.addHeader(contentType);
                            return httpPost;
                        }))
                .flatMap(httpPost -> constructLanguage(config)
                        .map(header -> {
                            httpPost.addHeader(header);
                            return httpPost;
                        }))
                .flatMap(httpPost -> SSLUtils.createSSLConnectionFactory()
                        .flatMap(sslConnectionSocketFactory -> NetworkUtils.constructHttpClient(sslConnectionSocketFactory))
                        .flatMap(httpClient -> executePostRequest(httpPost, httpClient)))
                .flatMap(httpResponse -> getHttpEntity(httpResponse))
                .flatMap(httpEntity -> FileUtils.getTempFile()
                        .flatMap(tempFile -> FileUtils.constructFileOutputStream(tempFile)
                                .flatMap(tempFileOutputStream -> writeToStream(httpEntity, tempFileOutputStream))
                                .flatMap(ignored -> Try.success(tempFile))));
    }

    private Try<Void> writeToStream(final HttpEntity httpEntity,
                                    final FileOutputStream tempFileOutputStream) {
        return Try.run(() -> httpEntity.writeTo(tempFileOutputStream));
    }

    private Try<HttpEntity> getHttpEntity(final HttpResponse httpResponse) {
        return Option.of(httpResponse.getEntity()).toTry();
    }

    private Try<HttpResponse> executePostRequest(final HttpPost httpPost,
                                                 final HttpClient httpClient) {
        return Try.of(() -> httpClient.execute(httpPost));
    }

    @Override
    public Try<File> convert(final File input,
                             final Conversion conversion) {
        return Try.run(() -> {
            Objects.requireNonNull(input, "input is null");
            Objects.requireNonNull(conversion, "conversion is null");
        }).flatMap(ignored -> convert(
                input,
                conversion,
                new DefaultConfig()
        ));
    }

    private Try<HttpPost> constructPostRequest(final Conversion conversion,
                                               final Config config) {
        return Try.run(() -> {
            Objects.requireNonNull(conversion, "conversion is null");
            Objects.requireNonNull(config, "config is null");
        }).flatMap(ignored -> Option.of(conversion.getUrl()).toTry())
                .flatMap(conversionUrl -> config.getUrl()
                        .map(baseUrl -> String.format("%s%s", baseUrl, conversionUrl)))
                .flatMap(url -> Try.of(() -> new HttpPost(url)));
    }

    private Try<Header> constructContentType(final File file) {
        return Option.of(file.getName()).toTry()
                .flatMap(fileName -> Try.of(() -> FilenameUtils.getExtension(fileName)))
                .map(fileExtenstion -> String.format("application/%s", fileExtenstion))
                .flatMap(contentType -> Try.of(() -> new BasicHeader(
                        "Content-Type",
                        contentType
                )));
    }

    private Try<Header> constructLanguage(final Config config) {
        return Try.run(() -> Objects.requireNonNull(config, "config is null"))
                .flatMap(ignored -> config.getLanguage())
                .flatMap(language -> Try.of(() -> new BasicHeader(
                        "Accept-Language",
                        language
                )));
    }
}
