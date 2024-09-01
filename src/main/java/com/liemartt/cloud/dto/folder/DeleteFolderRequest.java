package com.liemartt.cloud.dto.folder;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteFolderRequest {
    private String path;
    @NotNull(message = "Name must exist")
    private String folderName;
}
