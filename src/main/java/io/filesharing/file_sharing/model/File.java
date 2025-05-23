package io.filesharing.file_sharing.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    @Column(unique = true)
    private String id;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token;
    private String fileName;
    private LocalDateTime uploadedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime expiresAt;

}
