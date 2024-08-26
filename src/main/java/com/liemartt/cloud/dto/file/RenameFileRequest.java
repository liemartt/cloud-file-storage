package com.liemartt.cloud.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RenameFileRequest {
    private String path;
    private String oldName;
    private String newName;
}
