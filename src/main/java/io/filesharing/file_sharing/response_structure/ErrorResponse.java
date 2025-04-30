package io.filesharing.file_sharing.response_structure;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private String errors;
    private HttpStatus status;
    private LocalDateTime timeStamp;
}
