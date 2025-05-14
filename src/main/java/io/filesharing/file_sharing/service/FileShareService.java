package io.filesharing.file_sharing.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.filesharing.file_sharing.exceptions.DuplicateRandomIdException;
import io.filesharing.file_sharing.exceptions.FileEmptyException;
import io.filesharing.file_sharing.exceptions.FileExtensionNotAllowedException;
import io.filesharing.file_sharing.exceptions.FileStorageException;
import io.filesharing.file_sharing.exceptions.IdNotFoundException;
import io.filesharing.file_sharing.model.File;
import io.filesharing.file_sharing.repository.FileShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.*;

@Service
@RequiredArgsConstructor
public class FileShareService {
    private final FileShareRepository fileShareRepository;
    private final UuidService uuidService;
    private final FileCleanupService fileCleanupService;

    private static final Logger logger = LoggerFactory.getLogger(FileShareService.class);

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Value("#{'${file.allowed-extensions}'.split(',')}")
    private List<String> allowedExtensions;

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

    public void processFile(MultipartFile file)
            throws FileStorageException, FileEmptyException, FileExtensionNotAllowedException {

        String id = null;
        if (!checkFileExtensions(file.getOriginalFilename())) {
            logger.error("File extension not allowed");
            throw new FileExtensionNotAllowedException("File extension not allowed");
        }
        try {

            id = saveInDb(file);
            String[] ids = id.split("_");
            storeFile(file, ids[1]);

        } catch (Exception e) {
            logger.error("Failed to process file: {}", e.getMessage(), e);
            try {
                fileCleanupService.deleteDbRecordById(id);
            } catch (Exception deleteException) {
                logger.error("Rollback failed — couldn't delete DB record with id {}: {}", id,
                        deleteException.getMessage(), deleteException);
            }
            throw new FileStorageException("Failed to process file, reverted saving file", e);
        }
    }

    public boolean checkFileExtensions(String fileName) {
        String[] names = fileName.split("\\.");
        String extension = names[names.length - 1].toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            return false;
        }
        return true;
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
            return id;
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

    public Resource getFileById(String id) throws IdNotFoundException, FileNotFoundException, MalformedURLException {
        File file = fileShareRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("No file found for id: " + id));

        Path filePath = Paths.get(UPLOAD_DIR).resolve(file.getFileName());
        if (!Files.exists(filePath)) {
            logger.error("File not found, while fetching it for download");
            throw new FileNotFoundException("File not found on disk: " + filePath);
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            logger.error("Cannot read file while processing it for download");
            throw new FileNotFoundException("Cannot read file: " + filePath);
        }
        return resource;
    }
}
