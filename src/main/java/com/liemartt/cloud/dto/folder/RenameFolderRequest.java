package com.liemartt.cloud.dto.folder;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RenameFolderRequest {
    private String path;
    @NotNull(message = "Name must exist")
    private String oldName;
    @NotNull(message = "Name must exist")
    private String newName;
}
