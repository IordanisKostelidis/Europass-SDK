package eu.jnksoftware.europass.sdk.config;

import io.vavr.control.Try;

public class DefaultConfig implements Config {
    public Try<String> getUrl() {
        return Try.success("https://europass.cedefop.europa.eu/rest");
    }

    public Try<String> getLanguage() {
        return Try.success("en");
    }
}
