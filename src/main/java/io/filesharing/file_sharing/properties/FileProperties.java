package io.filesharing.file_sharing.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "file")
@Setter
@Getter
public class FileProperties {
    private String uploadDir;
}