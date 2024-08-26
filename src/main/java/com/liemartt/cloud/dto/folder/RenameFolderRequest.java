package com.liemartt.cloud.dto.folder;

import lombok.Data;

@Data
public class RenameFolderRequest {
    private String path;
    private String oldName;
    private String newName;
}
