package eu.jnksoftware.europass.sdk.utils;

import io.vavr.control.Try;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

public abstract class FileUtils {
    public static Try<File> constructFile(final String filePath) {
        return Try.run(() -> Objects.requireNonNull(filePath, "filePath is null"))
                .flatMap(ignored -> Try.of(() -> new File(filePath)));
    }

    public static Try<InputStream> constructInputStream(final File file) {
        return Try.run(() -> Objects.requireNonNull(file, "file is null"))
                .flatMap(ignored -> Try.of(() -> new FileInputStream(file)));
    }

    public static Try<File> getTempFile() {
        return Try.of(() -> File.createTempFile("europass-sdk","tmp"));
    }

    public static Try<FileOutputStream> constructFileOutputStream(final File file) {
        return Try.run(() -> Objects.requireNonNull(file, "filePath is null"))
                .flatMap(ignored -> Try.of(() -> new FileOutputStream(file)));
    }

    public static Try<FileOutputStream> constructFileOutputStream(final String filePath) {
        return Try.run(() -> Objects.requireNonNull(filePath, "filePath is null"))
                .flatMap(ignored -> Try.of(() -> new FileOutputStream(filePath)));
    }
}
