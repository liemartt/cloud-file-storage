package com.liemartt.cloud.dto.file;

import lombok.Data;

@Data
public class DownloadFileRequest {
    private String path;
    private String fileName;
}
