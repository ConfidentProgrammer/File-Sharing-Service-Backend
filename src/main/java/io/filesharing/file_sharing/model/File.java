package io.filesharing.file_sharing.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class File {
    @Id
    private long id;

    private String randomId;
    private String fileName;
    private LocalDateTime uploadedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime expiresAt;
}
