package io.filesharing.file_sharing.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.filesharing.file_sharing.repository.FileShareRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(FileShareService.class);
    private final FileShareRepository fileShareRepository;

    @Value("${file.upload-dir}")
    private static String dir;

    private static final Path UPLOAD_DIR = Paths.get(dir);

    public void deleteAllFiles() throws IOException {
        if (Files.exists(UPLOAD_DIR)) {
            try (Stream<Path> files = Files.walk(UPLOAD_DIR)) {
                files.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                logger.error("Error while deleting all files: {}", e.getMessage(), e);
                            }
                        });
            }
        }
    }
}
