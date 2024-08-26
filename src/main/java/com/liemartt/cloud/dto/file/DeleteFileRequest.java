package com.liemartt.cloud.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteFileRequest {
    private String path;
    private String fileName;
}
