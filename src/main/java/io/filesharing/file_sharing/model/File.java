package io.filesharing.file_sharing.model;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import io.filesharing.file_sharing.service.UuidService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class File {
    @Id
    private Long id;
    @Column(unique = true)
    private String randomId;
    private String fileName;
    private LocalDateTime uploadedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime expiresAt;
}
