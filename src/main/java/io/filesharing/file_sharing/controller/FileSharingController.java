package io.filesharing.file_sharing.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.filesharing.file_sharing.dto.UploadResult;
import io.filesharing.file_sharing.exceptions.DeleteFileException;
import io.filesharing.file_sharing.exceptions.DeleteRecordException;
import io.filesharing.file_sharing.exceptions.FileEmptyException;
import io.filesharing.file_sharing.exceptions.FileExtensionNotAllowedException;
import io.filesharing.file_sharing.exceptions.FileStorageException;
import io.filesharing.file_sharing.exceptions.IdNotFoundException;
import io.filesharing.file_sharing.orchestrators.FileShareOrchestrator;
import io.filesharing.file_sharing.response_structure.SuccessResponse;
import io.filesharing.file_sharing.service.FileCleanupService;
import io.filesharing.file_sharing.service.FileShareService;
import io.filesharing.file_sharing.service.TokenService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FileSharingController {

        private final FileCleanupService fileCleanupService;
        private final FileShareOrchestrator uploadFileOrchestrator;

        @PostMapping("/upload-file")
        public ResponseEntity<SuccessResponse> saveFile(@RequestParam("file") MultipartFile file)
                        throws FileStorageException, FileEmptyException, FileExtensionNotAllowedException {

                UploadResult uploadResult = uploadFileOrchestrator.uploadFile(file);
                String downloadLink = getLink(uploadResult.getTokenId());
                SuccessResponse successResponse = new SuccessResponse(true, HttpStatus.CREATED,
                                "File uploaded successfully, File download link: " + downloadLink);
                return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.CREATED);
        }

        @DeleteMapping("/delete-files")
        public ResponseEntity<SuccessResponse> deleteFile()
                        throws IOException, DeleteFileException, DeleteRecordException {
                fileCleanupService.deleteAllFiles();
                SuccessResponse successResponse = new SuccessResponse(true, HttpStatus.OK,
                                "All Files and DB records deleted successfully");
                return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.OK);
        }

        @DeleteMapping("/delete-file/{id}")
        public ResponseEntity<SuccessResponse> deleteFileById(@PathVariable String id)
                        throws IOException, DeleteFileException, DeleteRecordException, IdNotFoundException {
                fileCleanupService.deleteSpecificFile(id);
                SuccessResponse successResponse = new SuccessResponse(true, HttpStatus.OK,
                                "File " + id + " and DB record deleted successfully");
                return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.OK);
        }

        public String getLink(String endpoint) {
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                return String.format("%s/public/%s", baseUrl, endpoint);
        }

}
