package com.liemartt.cloud.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RenameFileRequest {
    @NotNull(message = "Name must not be empty")
    private String oldName;
    @NotNull(message = "Name must not be empty")
    private String newName;
}
