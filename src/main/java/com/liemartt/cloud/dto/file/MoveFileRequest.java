package com.liemartt.cloud.dto.file;

import lombok.Data;

@Data
public class MoveFileRequest {
    private String oldPath;
    private String newPath;
    private String fileName;
}
