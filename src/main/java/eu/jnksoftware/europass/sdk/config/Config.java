package eu.jnksoftware.europass.sdk.config;

import io.vavr.control.Try;

public interface Config {

    Try<String> getUrl();

    Try<String> getLanguage();
}
