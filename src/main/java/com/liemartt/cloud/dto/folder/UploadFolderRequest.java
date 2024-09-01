package com.liemartt.cloud.dto.folder;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFolderRequest {
    private String path;
    @NotNull(message = "Folder must exist")
    private MultipartFile[] folder;
}
