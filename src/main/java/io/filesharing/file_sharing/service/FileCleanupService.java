package io.filesharing.file_sharing.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import io.filesharing.file_sharing.exceptions.DeleteFileException;
import io.filesharing.file_sharing.exceptions.DeleteRecordException;
import io.filesharing.file_sharing.exceptions.IdNotFoundException;
import io.filesharing.file_sharing.model.File;
import io.filesharing.file_sharing.repository.FileShareRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(FileShareService.class);
    private final FileShareRepository fileShareRepository;

    @Value("${file.upload-dir}")
    private String dir;

    public void deleteAllFiles() throws IOException, DeleteFileException, DeleteRecordException {
        final Path UPLOAD_DIR = Paths.get(dir);

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
            } catch (Exception e) {
                throw new DeleteFileException("Error while deleting all files");
            }
        }
        try {
            fileShareRepository.deleteAll();
        } catch (Exception e) {
            logger.error("Error while deleting all db records: {}", e.getMessage(), e);
            throw new DeleteRecordException("Error while deleting all db records");
        }
    }

    public void deleteSpecificFile(String id) throws IdNotFoundException {
        File file = fileShareRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("No file found for id: " + id));

        final Path filePath = Paths.get(dir).resolve(file.getFileName());
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            deleteDbRecordById(id);
        } catch (IOException e) {
            logger.error("Error while deleting specific file", e.getMessage(), e);
        }
    }

    public void deleteDbRecordById(String id) throws IdNotFoundException {
        try {
            fileShareRepository.deleteById(id);
            logger.info("File record deleted successfully.");
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotFoundException("File not found. Nothing to delete.");
        } catch (Exception e) {
            logger.error("Unexpected error while deleting file with id {}: {}", id, e.getMessage(), e);
        }
    }
}
