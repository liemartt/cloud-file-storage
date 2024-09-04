package com.liemartt.cloud.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFileRequest {
    private String path;
    @NotNull(message = "Filename must exists")
    private String fileName;
}
