package io.filesharing.file_sharing.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import io.filesharing.file_sharing.exceptions.DuplicateRandomIdException;
import io.filesharing.file_sharing.exceptions.FileEmptyException;
import io.filesharing.file_sharing.exceptions.FileStorageException;
import io.filesharing.file_sharing.model.File;
import io.filesharing.file_sharing.repository.FileShareRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileShareService {

    private static final Logger logger = LoggerFactory.getLogger(FileShareService.class);
    private static final String UPLOAD_DIR = "src/uploads"; // Set your directory here

    private final FileShareRepository fileShareRepository;

    private final UuidService uuidService;

    public void storeFile(MultipartFile file, String randomId) throws FileStorageException, FileEmptyException {
        try {
            if (file.isEmpty()) {
                logger.error("Cannot upload empty file");
                throw new FileEmptyException("Cannot upload empty file");
            }
            String fileName = getFileName(file.getOriginalFilename(), randomId);
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

    private String getFileName(String originalFileName, String randomId) {
        String res = randomId + "_" + originalFileName;
        return res;
    }

    @Transactional
    public void processFile(MultipartFile file) throws FileStorageException {
        try {
            String randomId = saveInDb(file);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        storeFile(file, randomId);
                    } catch (Exception e) {
                        logger.error("Error while saving file: {}", e.getMessage(), e);
                        // TODO: retry if storing file fails
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Failed to process file: {}", e.getMessage(), e);
            throw new FileStorageException("Failed to process file, reverted saving file", e);
        }
    }

    public String saveInDb(MultipartFile file) throws DuplicateRandomIdException {
        Long timeStamp = uuidService.generateTimeStampId();
        String random = generateId(timeStamp);
        String id = timeStamp + "_" + random;
        try {
            File fileRecord = new File();
            fileRecord.setId(id);
            fileRecord.setFileName(random + "_" + file.getOriginalFilename());
            fileRecord.setUploadedAt(LocalDateTime.now());
            fileRecord.setDeletedAt(null);
            fileRecord.setExpiresAt(LocalDateTime.now().plusDays(7));
            fileShareRepository.save(fileRecord);
            logger.info("file record written to db");
            return random;
        } catch (DataIntegrityViolationException e) {
            logger.error("DB constraint violated while saving file record, RandomID already exists", e);
            throw new DuplicateRandomIdException("randomId already exists");
        }
    }

    public String generateId(long timeStamp) throws DuplicateRandomIdException {
        int retries = 3;
        for (int i = 0; i < retries; i++) {
            String random = uuidService.generateRandomId();
            String id = timeStamp + "_" + random;
            if (!fileShareRepository.existsById(id)) {
                return random;
            }
            logger.error("Duplicate randomID found: {}", random);
        }
        logger.error("Failed to generate Random Id after several attempts");
        throw new DuplicateRandomIdException("Failed to generate a unique random ID after several attempts");
    }
}
