package com.liemartt.cloud.dto.folder;

import lombok.Data;

@Data
public class DeleteFolderRequest {
    private String path;
    private String folderName;
}
