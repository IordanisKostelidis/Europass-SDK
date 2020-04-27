package eu.jnksoftware.europass.sdk.service;

import eu.jnksoftware.europass.sdk.config.Config;
import eu.jnksoftware.europass.sdk.constants.Conversion;
import io.vavr.control.Try;

import java.io.File;

public interface EuropassConvert {

    Try<File> convert(File input, Conversion conversion, Config config);

    Try<File> convert(File input, Conversion conversion);
}
