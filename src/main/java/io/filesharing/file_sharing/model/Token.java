package io.filesharing.file_sharing.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "token", indexes = {
        @Index(name = "idx_token_id", columnList = "id")
})
public class Token {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;
    private LocalDateTime expiresAt;

}
