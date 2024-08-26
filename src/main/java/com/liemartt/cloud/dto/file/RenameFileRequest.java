package com.liemartt.cloud.dto.file;

import lombok.Data;

@Data
public class RenameFileRequest {
    private String path;
    private String oldName;
    private String newName;
}
