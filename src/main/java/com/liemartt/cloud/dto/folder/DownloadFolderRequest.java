package com.liemartt.cloud.dto.folder;

import lombok.Data;

@Data
public class DownloadFolderRequest {
    private String path;
    private String folderName;
}
