package com.liemartt.cloud.dto.folder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateFolderRequest {
    private String path;
    @NotNull(message = "Name must exist")
    private String folderName;
}
