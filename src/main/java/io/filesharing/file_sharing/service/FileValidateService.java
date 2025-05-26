package io.filesharing.file_sharing.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.filesharing.file_sharing.exceptions.FileExtensionNotAllowedException;

@Service
public class FileValidateService {
    @Value("#{'${file.allowed-extensions}'.split(',')}")
    private List<String> allowedExtensions;

    private static final Logger logger = LoggerFactory.getLogger(FileValidateService.class);

    public void checkFileExtensions(String fileName) throws FileExtensionNotAllowedException {
        String[] names = fileName.split("\\.");
        String extension = names[names.length - 1].toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            logger.error("File extension not allowed");
            throw new FileExtensionNotAllowedException("File extension not allowed");
        }
    }

}
