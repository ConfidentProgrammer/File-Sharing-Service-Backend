package io.filesharing.file_sharing.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.filesharing.file_sharing.dto.DownloadResult;
import io.filesharing.file_sharing.exceptions.IdNotFoundException;
import io.filesharing.file_sharing.exceptions.TokenNotFoundException;
import io.filesharing.file_sharing.orchestrators.FileShareOrchestrator;
import io.filesharing.file_sharing.service.FileShareService;
import io.filesharing.file_sharing.service.TokenService;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("public")
public class PublicController {
        private final FileShareOrchestrator fileShareOrchestrator;

        @GetMapping("/{token}")
        public ResponseEntity<Resource> getFile(@PathVariable String token)
                        throws FileNotFoundException, MalformedURLException, IdNotFoundException,
                        TokenNotFoundException {
                DownloadResult downloadResult = fileShareOrchestrator.downloadFile(token);
                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + downloadResult.getFile().getFilename()
                                                                + "\"")
                                .body(downloadResult.getFile());
        }

}
