package io.filesharing.file_sharing.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Entity
@NoArgsConstructor
public class Token {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;
    private LocalDateTime expiresAt;

}
