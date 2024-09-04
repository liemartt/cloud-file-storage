package com.liemartt.cloud.dto.folder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFolderRequest {
    private String path;
    @NotNull(message = "Name must exist")
    private String folderName;
}
