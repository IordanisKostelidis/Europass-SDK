package eu.jnksoftware.europass.sdk.service;

import eu.jnksoftware.europass.sdk.constants.Conversion;
import eu.jnksoftware.europass.sdk.utils.FileUtils;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class EuropassConvertTest {

    private EuropassConvert europassConvert;

    @Before
    public void setUp() throws Exception {
        europassConvert = new EuropassConvertImpl();
    }

    @Test
    public void testXmlToJson_Success() {
        Try<File> files = Try.of(this::getClass)
                .map(Class::getClassLoader)
                .flatMap(classLoader -> Try.of(() -> classLoader.getResource("europass-cv-example-v3.4.0.xml")))
                .flatMap(url -> Option.of(url.getFile()).toTry())
                .flatMap(FileUtils::constructFile)
                .flatMap(file -> europassConvert.convert(
                        file,
                        Conversion.TO_JSON
                ));

        Assert.assertTrue(files.isSuccess());
    }
}