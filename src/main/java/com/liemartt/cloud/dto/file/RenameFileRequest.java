package com.liemartt.cloud.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameFileRequest {
    private String path;
    @NotNull(message = "Name must not be empty")
    private String oldName;
    @NotNull(message = "Name must not be empty")
    private String newName;
}
