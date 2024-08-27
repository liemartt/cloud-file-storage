package com.liemartt.cloud.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteFileRequest {
    @NotNull(message = "Filename must exists")
    private String fileName;
}
