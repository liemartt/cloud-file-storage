package com.liemartt.cloud.dto.folder;

import lombok.Data;

@Data
public class MoveFolderRequest {
    private String oldPath;
    private String newPath;
    private String folderName;
}
