package com.liemartt.cloud.dto.file;

import lombok.Data;

@Data
public class DeleteFileRequest {
    private String path;
    private String fileName;
}
