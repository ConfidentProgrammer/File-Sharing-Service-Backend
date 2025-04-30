package io.filesharing.file_sharing.response_structure;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SuccessResponse {
    private boolean success;
    private HttpStatus status;
    private String message;
}
