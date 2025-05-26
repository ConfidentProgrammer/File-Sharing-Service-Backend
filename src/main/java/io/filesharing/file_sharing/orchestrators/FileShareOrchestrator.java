package io.filesharing.file_sharing.orchestrators;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.filesharing.file_sharing.dto.DownloadResult;
import io.filesharing.file_sharing.dto.UploadResult;
import io.filesharing.file_sharing.exceptions.FileExtensionNotAllowedException;
import io.filesharing.file_sharing.exceptions.FileStorageException;
import io.filesharing.file_sharing.exceptions.IdNotFoundException;
import io.filesharing.file_sharing.exceptions.TokenNotFoundException;
import io.filesharing.file_sharing.model.File;
import io.filesharing.file_sharing.model.Token;
import io.filesharing.file_sharing.service.FileCleanupService;
import io.filesharing.file_sharing.service.FileShareService;
import io.filesharing.file_sharing.service.FileValidateService;
import io.filesharing.file_sharing.service.TokenService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileShareOrchestrator {
    private final FileValidateService fileValidateService;
    private final FileShareService fileShareService;
    private final TokenService tokenService;
    private final FileCleanupService fileCleanupService;
    private static final Logger logger = LoggerFactory.getLogger(FileShareOrchestrator.class);

    public UploadResult uploadFile(MultipartFile file) throws FileExtensionNotAllowedException, FileStorageException {
        fileValidateService.checkFileExtensions(file.getOriginalFilename());
        String id = null;
        try {
            File fileRecord = fileShareService.saveInDb(file);
            id = fileRecord.getId();
            String fileName = id.split("_")[1];
            fileShareService.storeFile(file, fileName);
            logger.info("File and file record saved successfully: {}", file.getOriginalFilename());
            Token token = tokenService.createTokenRecord(fileRecord);
            logger.info("Token record saved successfully: {}", token.getId());
            return new UploadResult(token.getId());
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

    public DownloadResult downloadFile(String token)
            throws TokenNotFoundException, FileNotFoundException, MalformedURLException, IdNotFoundException {
        String fileId = tokenService.getFileIdFromTokenId(token);
        Resource file = fileShareService.getFileById(fileId);
        return new DownloadResult(file);
    }
}
