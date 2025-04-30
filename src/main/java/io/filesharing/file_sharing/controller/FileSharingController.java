package io.filesharing.file_sharing.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.filesharing.file_sharing.exceptions.FileEmptyException;
import io.filesharing.file_sharing.exceptions.FileStorageException;
import io.filesharing.file_sharing.response_structure.SuccessResponse;
import io.filesharing.file_sharing.service.FileShareService;
import io.filesharing.file_sharing.service.UuidService;

import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1")
public class FileSharingController {

    @Autowired
    private FileShareService fileShareService;

    @Autowired
    private UuidService uuidService;

    @PostMapping("/save-file")
    public ResponseEntity<SuccessResponse> saveFile(@RequestParam("file") MultipartFile file)
            throws FileStorageException, FileEmptyException {

        fileShareService.processFile(file);
        SuccessResponse successResponse = new SuccessResponse(true, HttpStatus.CREATED, "File uploaded successfully");
        return new ResponseEntity<SuccessResponse>(successResponse, HttpStatus.CREATED);
    }
}
