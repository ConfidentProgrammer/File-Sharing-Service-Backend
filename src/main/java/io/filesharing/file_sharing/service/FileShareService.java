package io.filesharing.file_sharing.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.filesharing.file_sharing.exceptions.FileEmptyException;
import io.filesharing.file_sharing.exceptions.FileStorageException;
import io.filesharing.file_sharing.repository.FileShareRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileShareService {
    private static final Logger logger = LoggerFactory.getLogger(FileShareService.class);
    private static final String UPLOAD_DIR = "src/uploads"; // Set your directory here

    private final FileShareRepository fileShareRepository;

    public void storeFile(MultipartFile file) throws FileStorageException, FileEmptyException {
        try {
            if (file.isEmpty()) {
                logger.error("Cannot upload empty file");
                throw new FileEmptyException("Cannot upload empty file");
            }
            String fileName = file.getOriginalFilename();
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            logger.info("File uploaded successfully: {}", file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("Error while saving file: {}", e.getMessage(), e);
            throw new FileStorageException("Error while saving the file");
        }
    }

    public void processFile(MultipartFile file) throws FileStorageException {
        try {
            // write in the db

            // store the file
            storeFile(file);
        } catch (Exception e) {
            logger.error("Failed to process file: {}", e.getMessage(), e);

            // revert the db record

            throw new FileStorageException("Failed to process file", e);
        }
    }

    public void saveInDb(MultipartFile file) {
        // generate Timestamp
        // generate randomId, if already present in the db generate another one. may be
        // in loop.
        // generate all the columns.
    }
}
