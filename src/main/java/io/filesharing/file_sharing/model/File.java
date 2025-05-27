package io.filesharing.file_sharing.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "file", indexes = {
        @Index(name = "idx_file_id", columnList = "id"),
})
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
