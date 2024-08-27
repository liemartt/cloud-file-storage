package com.liemartt.cloud.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DownloadFileRequest {
    @NotNull(message = "Filename must exists")
    private String fileName;
}
