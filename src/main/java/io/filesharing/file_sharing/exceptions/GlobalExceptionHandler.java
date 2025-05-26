package io.filesharing.file_sharing.exceptions;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import io.filesharing.file_sharing.response_structure.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        logger.warn("File not found: {}", ex.getMessage());
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleSizeExceedException(MaxUploadSizeExceededException ex) {
        logger.warn("File limit exceed: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FileEmptyException.class)
    public ResponseEntity<ErrorResponse> handleFileEmptyException(FileEmptyException ex) {
        logger.warn("File is empty: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex) {
        logger.warn("File storage error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DuplicateRandomIdException.class)
    public ResponseEntity<ErrorResponse> DuplicateRandomIdException(DuplicateRandomIdException ex) {
        logger.warn("Duplicated randomID exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorResponse> IdNotFoundException(IdNotFoundException ex) {
        logger.warn("Id not found exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeleteFileException.class)
    public ResponseEntity<ErrorResponse> DeleteFileException(DeleteFileException ex) {
        logger.warn("Deleting file exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteRecordException.class)
    public ResponseEntity<ErrorResponse> DeleteRecordException(DeleteRecordException ex) {
        logger.warn("Deleting records exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileExtensionNotAllowedException.class)
    public ResponseEntity<ErrorResponse> FileExtensionNotAllowedException(FileExtensionNotAllowedException ex) {
        logger.warn("File extension not allowed: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TokenStorageException.class)
    public ResponseEntity<ErrorResponse> TokenStorageException(TokenStorageException ex) {
        logger.warn("Error while creating token: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> TokenNotFoundException(TokenNotFoundException ex) {
        logger.warn("Token not found in the db: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage().toString(),
                HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}