package io.filesharing.file_sharing.dto;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class DownloadResult {
    private Resource file;
}
